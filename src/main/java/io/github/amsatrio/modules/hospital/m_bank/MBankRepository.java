package io.github.amsatrio.modules.hospital.m_bank;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MBankRepository implements PanacheRepository<MBank> {

    private static final String TABLE = "m_bank";

    private static final String SELECT_ALL = "SELECT id, name, va_code, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM m_bank";

    public MBank findById(Long id) {
        return (MBank) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", MBank.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<MBank> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, MBank.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<MBank> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, MBank.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setParameter("limit", pageSize);
        query.setParameter("offset", pageIndex * pageSize);
        return query.getResultList();
    }

    public long countByFilter(String whereClause, Map<String, Object> params) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE " + whereClause;
        var query = getEntityManager()
                .createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return (Long) query.getSingleResult();
    }

    public void insert(MBank data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, name, va_code, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :name, :va_code, :created_by, :created_on, :modified_by, :modified_on, :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("name", data.getName())
                .setParameter("va_code", data.getVaCode())
                .setParameter("created_by", data.getCreatedBy())
                .setParameter("created_on", data.getCreatedOn())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .executeUpdate();
    }

    public void update(MBank data) {
        String sql = "UPDATE " + TABLE
                + " SET name = :name, va_code = :va_code, modified_by = :modified_by, modified_on = :modified_on, deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("name", data.getName())
                .setParameter("va_code", data.getVaCode())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .setParameter("id", data.getId())
                .executeUpdate();
    }

    public void softDelete(Long id, Long userId) {
        String sql = "UPDATE " + TABLE
                + " SET is_delete = true, deleted_by = :userId, deleted_on = :deletedOn"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .setParameter("deletedOn", new java.util.Date())
                .executeUpdate();
    }

    public void hardDelete(Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", id)
                .executeUpdate();
    }

    public static String toColumnName(String fieldName) {
        return switch (fieldName) {
            case "vaCode" -> "va_code";
            case "createdBy" -> "created_by";
            case "createdOn" -> "created_on";
            case "modifiedBy" -> "modified_by";
            case "modifiedOn" -> "modified_on";
            case "deletedBy" -> "deleted_by";
            case "deletedOn" -> "deleted_on";
            case "isDelete" -> "is_delete";
            default -> fieldName;
        };
    }
}
