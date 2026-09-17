#!/usr/bin/env python3
"""
Generate Quarkus CRUD (Entity + Repository + API) classes for db-hospital tables.

For every table whose name starts with one of the configured prefixes (default
``m_`` and ``t_``) this script emits three files inside the target package:

    <CamelName>.java             JPA entity
    <CamelName>Repository.java   Panache repository (native queries)
    <CamelName>Api.java          JAX-RS resource: CRUD + pagination + sort + filter/search

The generated code follows the existing ``MBiodata`` module convention:

    GET    /v1/<table-with-dashes>/{id}   get by id
    POST   /v1/<table-with-dashes>        create (id optional for AUTO_INCREMENT tables)
    PUT    /v1/<table-with-dashes>        update
    DELETE /v1/<table-with-dashes>/{id}   hard delete
    GET    /v1/<table-with-dashes>        pagination + sort + filter/search
                                          ?page=0&size=5
                                          &sort=[{"id":"name","desc":true}]
                                          &filter=[{"id":"name","value":"ab",
                                                    "matchMode":"CONTAINS",
                                                    "dataType":"TEXT"}]

Schema input
------------
The generator reads a TSV with the columns:

    TABLE_NAME <TAB> COLUMN_NAME <TAB> COLUMN_TYPE <TAB> IS_NULLABLE <TAB> COLUMN_KEY

By default it loads the bundled snapshot ``scripts/hospital_schema.tsv``.
Use ``--schema`` to point at another file, or pass ``--db-*`` options to dump
the schema from a live MySQL/MariaDB database using the ``mysql`` CLI:

    SELECT TABLE_NAME, COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_KEY
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = 'db-hospital'
    ORDER BY TABLE_NAME, ORDINAL_POSITION;

Examples
--------
    # regenerate using the bundled schema snapshot (run from anywhere)
    python3 scripts/generate_hospital_crud.py

    # use a custom schema file / output directory
    python3 scripts/generate_hospital_crud.py \
        --schema scripts/hospital_schema.tsv \
        --output src/main/java/io/github/amsatrio/modules/hospital

    # dump the schema from the database first (password via env MYSQL_PWD is preferred)
    python3 scripts/generate_hospital_crud.py \
        --db-name db-hospital --db-user user_local --db-password secret \
        --db-host 127.0.0.1 --db-port 3306
"""

import argparse
import os
import re
import subprocess
import sys
from pathlib import Path

AUDIT = {
    "created_by", "created_on", "modified_by", "modified_on",
    "deleted_by", "deleted_on", "is_delete",
}
AUDIT_ORDER = [
    "created_by", "created_on", "modified_by", "modified_on",
    "deleted_by", "deleted_on", "is_delete",
]
AUDIT_DEF = [
    ("created_by", "bigint", "Long", "createdBy"),
    ("created_on", "datetime", "Date", "createdOn"),
    ("modified_by", "bigint", "Long", "modifiedBy"),
    ("modified_on", "datetime", "Date", "modifiedOn"),
    ("deleted_by", "bigint", "Long", "deletedBy"),
    ("deleted_on", "datetime", "Date", "deletedOn"),
    ("is_delete", "tinyint(1)", "Boolean", "isDelete"),
]
DATE_TS = ["datetime", "timestamp"]


# --------------------------------------------------------------------------- #
# naming / type helpers
# --------------------------------------------------------------------------- #
def camel(col):
    parts = col.split("_")
    return parts[0] + "".join(p.capitalize() for p in parts[1:])


def upper_first(s):
    return s[0].upper() + s[1:]


def class_name(table):
    return "".join(p.capitalize() for p in table.split("_"))


def java_type(c_type):
    c_type = c_type.lower()
    if c_type.startswith("bigint"):
        return "Long"
    if c_type.startswith("int"):
        return "Integer"
    if (c_type.startswith("varchar") or c_type == "text"
            or c_type.startswith("char") or c_type.startswith("tinytext")
            or c_type.startswith("longtext")):
        return "String"
    if (c_type.startswith("mediumblob") or c_type.startswith("longblob")
            or c_type.startswith("blob") or c_type.startswith("binary")):
        return "byte[]"
    if (c_type.startswith("decimal") or c_type.startswith("numeric")
            or c_type.startswith("float") or c_type.startswith("double")):
        return "BigDecimal"
    if c_type.startswith("date") or c_type in DATE_TS:
        return "Date"
    if c_type.startswith("tinyint") or c_type.startswith("boolean") or c_type.startswith("bit"):
        return "Boolean"
    return "String"


def is_date(c_type):
    return c_type.lower().startswith("date") or c_type.lower() in DATE_TS


def is_datetime(c_type):
    return c_type.lower() in DATE_TS


def col_def(col, c_type):
    if col == "is_delete":
        return "boolean comment 'default FALSE'"
    if col == "id":
        return "bigint"
    return c_type.lower()


# --------------------------------------------------------------------------- #
# entity
# --------------------------------------------------------------------------- #
def build_entity(table, columns, package):
    name = class_name(table)
    need_date = any(is_date(ct) for _, ct in columns)
    need_bd = any(java_type(ct) == "BigDecimal" for _, ct in columns)

    imp = [f"package {package}.{table};", ""]
    if need_date:
        imp.append("import java.util.Date;")
    if need_bd:
        imp.append("import java.math.BigDecimal;")
    if need_date or need_bd:
        imp.append("")
    imp += [
        "import org.hibernate.validator.constraints.Length;",
        "",
        "import com.fasterxml.jackson.annotation.JsonFormat;",
        "import com.fasterxml.jackson.annotation.JsonProperty;",
        "",
        "import jakarta.persistence.Column;",
        "import jakarta.persistence.Entity;",
        "import jakarta.persistence.Id;",
        "import jakarta.persistence.Table;",
        "import jakarta.validation.constraints.NotNull;",
        "",
        "",
    ]

    fields = []
    for col, ctype in columns:
        if col == "id":
            fields.append("    @Id\n    @NotNull(message = \"id is mandatory\")\n"
                          "    @JsonProperty(\"id\")\n"
                          "    @Column(name = \"id\", columnDefinition = \"bigint\")\n"
                          "    private Long id;\n")
            continue
        if col == "created_by":
            fields.append("    @NotNull(message = \"created_by is mandatory\")\n"
                          "    @JsonProperty(\"createdBy\")\n"
                          "    @Column(name = \"created_by\", columnDefinition = \"bigint\")\n"
                          "    private Long createdBy;\n")
            continue
        if col == "created_on":
            fields.append("    @NotNull(message = \"created_on is mandatory\")\n"
                          "    @JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n"
                          "    @JsonProperty(\"createdOn\")\n"
                          "    @Column(name = \"created_on\", columnDefinition = \"datetime\")\n"
                          "    private Date createdOn;\n")
            continue
        if col == "is_delete":
            fields.append("    @NotNull(message = \"is_delete is mandatory\")\n"
                          "    @JsonProperty(\"isDelete\")\n"
                          "    @Column(name = \"is_delete\", columnDefinition = \"boolean comment 'default FALSE'\")\n"
                          "    private Boolean isDelete = false;\n")
            continue

        prop = camel(col)
        jt = java_type(ctype)
        lines = []
        m = re.match(r"varchar\((\d+)\)", ctype.lower())
        if jt == "String" and m:
            n = m.group(1)
            lines.append(f"    @Length(max = {n}, message = \"{prop} must be between 0-{n} characters\")")
        if is_date(ctype):
            pat = "yyyy-MM-dd" if not is_datetime(ctype) else "yyyy-MM-dd HH:mm:ss"
            lines.append(f'    @JsonFormat(pattern = "{pat}")')
        lines.append(f'    @JsonProperty("{prop}")')
        lines.append(f'    @Column(name = "{col}", columnDefinition = "{col_def(col, ctype)}")')
        lines.append(f"    private {jt} {prop};\n")
        fields.append("\n".join(lines))

    # add any audit column that is not part of the table (defensive; usually none)
    for col, ctype, jt, prop in AUDIT_DEF:
        if col in dict(columns):
            continue
        fields.append(f'    @JsonProperty("{prop}")\n'
                      f'    @Column(name = "{col}", columnDefinition = "{col_def(col, ctype)}")\n'
                      f"    private {jt} {prop};\n")

    accessors = []
    for col, ctype in columns:
        prop = camel(col)
        jt = java_type(ctype)
        g = upper_first(prop)
        accessors.append(f"    public {jt} get{g}() {{\n        return {prop};\n    }}\n"
                         f"    public void set{g}({jt} {prop}) {{\n        this.{prop} = {prop};\n    }}\n")

    return "\n".join(imp) + f"""@Entity
@Table(name = "{table}")
public class {name} {{

{''.join(fields)}

{''.join(accessors)}}}
"""


# --------------------------------------------------------------------------- #
# repository
# --------------------------------------------------------------------------- #
def build_repository(table, columns, package):
    name = class_name(table)
    business = [col for col, _ in columns if col not in AUDIT and col != "id"]
    audit = AUDIT_ORDER
    select_all = ("SELECT id" + (", " + ", ".join(business) if business else "") + ", "
                  + ", ".join(audit) + " FROM " + table)
    insert_cols = ["id"] + business + audit
    update_cols = business + ["modified_by", "modified_on", "deleted_by", "deleted_on", "is_delete"]

    gprops = {col: upper_first(camel(col)) for col, _ in columns}

    insert_values = ", ".join(f":{c}" for c in insert_cols)
    set_clause = ", ".join(f"{c} = :{c}" for c in update_cols)

    insert_setters = [f'                .setParameter("{c}", data.get{gprops[c]}())' for c in insert_cols]
    update_setters = [f'                .setParameter("{c}", data.get{gprops[c]}())'
                      for c in update_cols + ["id"]]

    switch = []
    for col, _ in columns:
        prop = camel(col)
        if prop != col:
            switch.append(f'            case "{prop}" -> "{col}";')
    switch.append("            default -> fieldName;")

    return f"""package {package}.{table};

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class {name}Repository implements PanacheRepository<{name}> {{

    private static final String TABLE = "{table}";

    private static final String SELECT_ALL = "{select_all}";

    public {name} findById(Long id) {{
        return ({name}) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", {name}.class)
                .setParameter("id", id)
                .getSingleResult();
    }}

    public List<{name}> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {{
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, {name}.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }}

    public long countAll() {{
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }}

    public List<{name}> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {{
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, {name}.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {{
            query.setParameter(entry.getKey(), entry.getValue());
        }}
        query.setParameter("limit", pageSize);
        query.setParameter("offset", pageIndex * pageSize);
        return query.getResultList();
    }}

    public long countByFilter(String whereClause, Map<String, Object> params) {{
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE " + whereClause;
        var query = getEntityManager()
                .createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {{
            query.setParameter(entry.getKey(), entry.getValue());
        }}
        return (Long) query.getSingleResult();
    }}

    public void insert({name} data) {{
        String sql = "INSERT INTO " + TABLE
                + " ({", ".join(insert_cols)})"
                + " VALUES"
                + " ({insert_values})";
        getEntityManager()
                .createNativeQuery(sql)
{chr(10).join(insert_setters)}
                .executeUpdate();
    }}

    public void update({name} data) {{
        String sql = "UPDATE " + TABLE
                + " SET {set_clause}"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
{chr(10).join(update_setters)}
                .executeUpdate();
    }}

    public void softDelete(Long id, Long userId) {{
        String sql = "UPDATE " + TABLE
                + " SET is_delete = true, deleted_by = :userId, deleted_on = :deletedOn"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .setParameter("deletedOn", new java.util.Date())
                .executeUpdate();
    }}

    public void hardDelete(Long id) {{
        String sql = "DELETE FROM " + TABLE + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", id)
                .executeUpdate();
    }}

    public static String toColumnName(String fieldName) {{
        return switch (fieldName) {{
{chr(10).join(switch)}
        }};
    }}
}}
"""


# --------------------------------------------------------------------------- #
# api
# --------------------------------------------------------------------------- #
def build_api(table, columns, package):
    name = class_name(table)
    low = name[0].lower() + name[1:]
    repo = f"{low}Repository"
    slug = table.replace("_", "-")
    business = [camel(col) for col, _ in columns if col not in AUDIT and col != "id"]
    upd = "\n".join(f"        entity.set{upper_first(p)}(data.get{upper_first(p)}());" for p in business)

    return f"""package {package}.{table};

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.amsatrio.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.dto.exception.DataExistException;
import io.github.amsatrio.dto.exception.NotFoundException;
import io.github.amsatrio.dto.request.FilterRequest;
import io.github.amsatrio.dto.request.SortRequest;
import io.github.amsatrio.dto.response.AppResponse;
import io.github.amsatrio.dto.response.PaginationResponse;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/v1/{slug}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class {name}Api {{
    @Inject
    private {name}Repository {repo};

    @GET
    @Path("/{{id}}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<{name}> getById(@PathParam("id") Long id) {{
        {name} entity = null;
        try {{
            entity = {repo}.findById(id);
        }} catch (NoResultException e) {{
            throw new NotFoundException("data not found");
        }}
        return AppResponse.ok(entity);
    }}

    @DELETE
    @Path("/{{id}}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<{name}> deleteById(@PathParam("id") Long id) {{
        try {{
            {repo}.findById(id);
        }} catch (NoResultException e) {{
            throw new NotFoundException("data not found");
        }}
        {repo}.hardDelete(id);
        return AppResponse.ok(null);
    }}

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<{name}> create({name} data) {{
        if (data.getId() != null) {{
            try {{
                {name} existing = {repo}.findById(data.getId());
                if (existing != null) {{
                    throw new DataExistException("data exists");
                }}
            }} catch (NoResultException e) {{
                // expected - data does not exist
            }}
        }}

        Long accessUserId = 0L;
        data.setCreatedBy(accessUserId);
        data.setCreatedOn(new Date());
        data.setModifiedBy(null);
        data.setModifiedOn(null);
        data.setDeletedBy(null);
        data.setDeletedOn(null);
        data.setIsDelete(false);

        {repo}.insert(data);
        return AppResponse.ok(null);
    }}

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<{name}> update({name} data) {{
        {name} entity = null;
        try {{
            entity = {repo}.findById(data.getId());
        }} catch (NoResultException e) {{
            throw new NotFoundException("data not found");
        }}

        Long accessUserId = 0L;
        entity.setModifiedBy(accessUserId);
        entity.setModifiedOn(new Date());
        entity.setDeletedBy(null);
        entity.setDeletedOn(null);
        entity.setIsDelete(data.getIsDelete());
        if (Boolean.TRUE.equals(entity.getIsDelete())) {{
            entity.setDeletedBy(accessUserId);
            entity.setDeletedOn(new Date());
        }}

{upd}

        {repo}.update(entity);
        return AppResponse.ok(null);
    }}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<PaginationResponse<{name}>> getPagination(@QueryParam("page") Integer pageIndex,
            @QueryParam("size") Integer pageSize, @QueryParam("sort") String sortRequestString,
            @QueryParam("filter") String filterRequestString) {{

        if (pageIndex == null) {{
            pageIndex = 0;
        }}
        if (pageSize == null) {{
            pageSize = 5;
        }}

        String sortColumn = "id";
        boolean sortAsc = true;
        if (sortRequestString != null) {{
            SortRequest sortRequest = SortRequest.from(sortRequestString).getFirst();
            sortColumn = {name}Repository.toColumnName(sortRequest.getId());
            sortAsc = !sortRequest.isDesc();
        }}

        List<FilterRequest> filterRequests = new ArrayList<>();
        if (filterRequestString != null) {{
            filterRequests = FilterRequest.from(filterRequestString);
        }}

        List<{name}> data = new ArrayList<>();
        long totalData = 0L;

        if (filterRequests.isEmpty()) {{
            data = {repo}.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = {repo}.countAll();
        }} else {{
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {{
                FilterRequest filterRequest = filterRequests.get(i);
                String column = {name}Repository.toColumnName(filterRequest.getId());
                String paramName = "p" + i;

                if (i != 0) {{
                    whereClause.append(" AND ");
                }}

                whereClause.append(column);
                switch (filterRequest.getMatchMode()) {{
                    case FilterMatchMode.CONTAINS:
                        whereClause.append(" LIKE :").append(paramName);
                        params.put(paramName, "%" + filterRequest.getValue() + "%");
                        break;
                    case FilterMatchMode.EQUALS:
                        whereClause.append(" = :").append(paramName);
                        params.put(paramName, filterRequest.getValue());
                        break;
                    case FilterMatchMode.NOT:
                        whereClause.append(" <> :").append(paramName);
                        params.put(paramName, filterRequest.getValue());
                        break;
                    case FilterMatchMode.LESS_THAN:
                        whereClause.append(" < :").append(paramName);
                        params.put(paramName, filterRequest.getValue());
                        break;
                    case FilterMatchMode.GREATER_THAN:
                        whereClause.append(" > :").append(paramName);
                        params.put(paramName, filterRequest.getValue());
                        break;
                    default:
                        whereClause.append(" LIKE :").append(paramName);
                        params.put(paramName, "%" + filterRequest.getValue() + "%");
                        break;
                }}
            }}

            data = {repo}.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = {repo}.countByFilter(whereClause.toString(), params);
        }}

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {{
            totalPages++;
        }}

        PaginationResponse<{name}> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return AppResponse.ok(paginationResponse);
    }}
}}
"""


# --------------------------------------------------------------------------- #
# schema loading
# --------------------------------------------------------------------------- #
def parse_schema_lines(lines):
    """Return an ordered dict {table: [(column, type), ...]} from TSV lines."""
    tables = {}
    for line in lines:
        line = line.rstrip("\r\n")
        if not line or line.startswith("TABLE_NAME"):
            continue
        parts = line.split("\t")
        if len(parts) < 3:
            continue
        table, column, ctype = parts[0], parts[1], parts[2]
        tables.setdefault(table, []).append((column, ctype))
    return tables


def load_schema_from_file(path):
    with open(path) as fh:
        return parse_schema_lines(fh)


def load_schema_from_db(args):
    query = (
        "SELECT TABLE_NAME, COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_KEY "
        "FROM INFORMATION_SCHEMA.COLUMNS "
        f"WHERE TABLE_SCHEMA = '{args.db_name}' "
        "ORDER BY TABLE_NAME, ORDINAL_POSITION"
    )
    cmd = [
        "mysql", "--skip-ssl", "--protocol=TCP",
        "-h", args.db_host, "-P", str(args.db_port),
        "-u", args.db_user, "-N", "-B", args.db_name, "-e", query,
    ]
    env = dict(os.environ)
    if args.db_password:
        env["MYSQL_PWD"] = args.db_password
    proc = subprocess.run(cmd, capture_output=True, text=True, env=env)
    if proc.returncode != 0:
        sys.stderr.write(proc.stderr)
        raise SystemExit(f"mysql exited with code {proc.returncode}")
    return parse_schema_lines(proc.stdout.splitlines())


# --------------------------------------------------------------------------- #
# main
# --------------------------------------------------------------------------- #
def default_paths():
    script_dir = Path(__file__).resolve().parent
    repo_root = script_dir.parent
    return (
        script_dir / "hospital_schema.tsv",
        repo_root / "src/main/java/io/github/amsatrio/modules/hospital",
    )


def main():
    default_schema, default_output = default_paths()

    parser = argparse.ArgumentParser(description=__doc__,
                                     formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--schema", default=str(default_schema),
                        help="TSV schema file (default: %(default)s)")
    parser.add_argument("--output", default=str(default_output),
                        help="target root package directory (default: %(default)s)")
    parser.add_argument("--package", default="io.github.amsatrio.modules.hospital",
                        help="Java package prefix (default: %(default)s)")
    parser.add_argument("--prefix", action="append", default=None,
                        help="table prefix to generate (repeatable; default: m_ and t_)")
    parser.add_argument("--skip", action="append", default=None,
                        help="table to skip (repeatable; default: m_biodata)")
    parser.add_argument("--db-name", help="dump schema from this database instead of --schema")
    parser.add_argument("--db-host", default="127.0.0.1")
    parser.add_argument("--db-port", type=int, default=3306)
    parser.add_argument("--db-user", default="root")
    parser.add_argument("--db-password", default=os.environ.get("MYSQL_PWD", ""))
    args = parser.parse_args()

    prefixes = tuple(args.prefix) if args.prefix else ("m_", "t_")
    skip = set(args.skip) if args.skip else {"m_biodata"}

    if args.db_name:
        tables = load_schema_from_db(args)
    else:
        tables = load_schema_from_file(args.schema)

    if not tables:
        raise SystemExit("no tables found in schema input")

    created = 0
    for table in sorted(tables):
        if table in skip or not table.startswith(prefixes):
            continue
        columns = tables[table]
        out_dir = Path(args.output) / table
        out_dir.mkdir(parents=True, exist_ok=True)
        name = class_name(table)
        (out_dir / f"{name}.java").write_text(build_entity(table, columns, args.package))
        (out_dir / f"{name}Repository.java").write_text(build_repository(table, columns, args.package))
        (out_dir / f"{name}Api.java").write_text(build_api(table, columns, args.package))
        created += 1
        print(f"generated {table} -> {name}")

    print(f"done: {created} table(s), {created * 3} file(s) under {args.output}")


if __name__ == "__main__":
    main()
