package io.github.amsatrio.modules.hospital.m_customer;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MCustomerRepository implements PanacheRepository<MCustomer> {

    private static final String TABLE = "m_customer";

    private static final String SELECT_ALL = "SELECT id, biodata_id, dob, gender, blood_group_id, rhesus_type, height, weight, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM m_customer";

    public MCustomer findById(Long id) {
        return (MCustomer) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", MCustomer.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<MCustomer> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, MCustomer.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<MCustomer> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, MCustomer.class);
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

    public void insert(MCustomer data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, biodata_id, dob, gender, blood_group_id, rhesus_type, height, weight, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :biodata_id, :dob, :gender, :blood_group_id, :rhesus_type, :height, :weight, :created_by, :created_on, :modified_by, :modified_on, :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("biodata_id", data.getBiodataId())
                .setParameter("dob", data.getDob())
                .setParameter("gender", data.getGender())
                .setParameter("blood_group_id", data.getBloodGroupId())
                .setParameter("rhesus_type", data.getRhesusType())
                .setParameter("height", data.getHeight())
                .setParameter("weight", data.getWeight())
                .setParameter("created_by", data.getCreatedBy())
                .setParameter("created_on", data.getCreatedOn())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .executeUpdate();
    }

    public void update(MCustomer data) {
        String sql = "UPDATE " + TABLE
                + " SET biodata_id = :biodata_id, dob = :dob, gender = :gender, blood_group_id = :blood_group_id, rhesus_type = :rhesus_type, height = :height, weight = :weight, modified_by = :modified_by, modified_on = :modified_on, deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("biodata_id", data.getBiodataId())
                .setParameter("dob", data.getDob())
                .setParameter("gender", data.getGender())
                .setParameter("blood_group_id", data.getBloodGroupId())
                .setParameter("rhesus_type", data.getRhesusType())
                .setParameter("height", data.getHeight())
                .setParameter("weight", data.getWeight())
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
            case "biodataId" -> "biodata_id";
            case "bloodGroupId" -> "blood_group_id";
            case "rhesusType" -> "rhesus_type";
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
