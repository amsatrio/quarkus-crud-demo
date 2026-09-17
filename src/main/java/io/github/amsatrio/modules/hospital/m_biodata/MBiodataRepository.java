package io.github.amsatrio.modules.hospital.m_biodata;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MBiodataRepository implements PanacheRepository<MBiodata> {

    private static final String TABLE = "m_biodata";

    private static final String SELECT_ALL = "SELECT id, fullname, mobile_phone, image, image_path, "
            + "created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM " + TABLE;

    public MBiodata findById(Long id) {
        return (MBiodata) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", MBiodata.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<MBiodata> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
                
        var query = getEntityManager()
                .createNativeQuery(sql, MBiodata.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<MBiodata> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, MBiodata.class);
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

    public void insert(MBiodata data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, fullname, mobile_phone, image, image_path,"
                + " created_by, created_on, modified_by, modified_on,"
                + " deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :fullname, :mobile_phone, :image, :image_path,"
                + " :created_by, :created_on, :modified_by, :modified_on,"
                + " :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("fullname", data.getFullname())
                .setParameter("mobile_phone", data.getMobilePhone())
                .setParameter("image", data.getImage())
                .setParameter("image_path", data.getImagePath())
                .setParameter("created_by", data.getCreatedBy())
                .setParameter("created_on", data.getCreatedOn())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .executeUpdate();
    }

    public void update(MBiodata data) {
        String sql = "UPDATE " + TABLE
                + " SET fullname = :fullname, mobile_phone = :mobile_phone,"
                + " image = :image, image_path = :image_path,"
                + " modified_by = :modified_by, modified_on = :modified_on,"
                + " deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("fullname", data.getFullname())
                .setParameter("mobile_phone", data.getMobilePhone())
                .setParameter("image", data.getImage())
                .setParameter("image_path", data.getImagePath())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
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
            case "mobilePhone" -> "mobile_phone";
            case "imagePath" -> "image_path";
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
