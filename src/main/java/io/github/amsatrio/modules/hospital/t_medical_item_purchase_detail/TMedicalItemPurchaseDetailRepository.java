package io.github.amsatrio.modules.hospital.t_medical_item_purchase_detail;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TMedicalItemPurchaseDetailRepository implements PanacheRepository<TMedicalItemPurchaseDetail> {

    private static final String TABLE = "t_medical_item_purchase_detail";

    private static final String SELECT_ALL = "SELECT id, medical_item_purchase_id, medical_item_id, qty, medical_facility_id, courir_id, sub_total, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM t_medical_item_purchase_detail";

    public TMedicalItemPurchaseDetail findById(Long id) {
        return (TMedicalItemPurchaseDetail) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", TMedicalItemPurchaseDetail.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<TMedicalItemPurchaseDetail> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, TMedicalItemPurchaseDetail.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<TMedicalItemPurchaseDetail> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, TMedicalItemPurchaseDetail.class);
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

    public void insert(TMedicalItemPurchaseDetail data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, medical_item_purchase_id, medical_item_id, qty, medical_facility_id, courir_id, sub_total, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :medical_item_purchase_id, :medical_item_id, :qty, :medical_facility_id, :courir_id, :sub_total, :created_by, :created_on, :modified_by, :modified_on, :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("medical_item_purchase_id", data.getMedicalItemPurchaseId())
                .setParameter("medical_item_id", data.getMedicalItemId())
                .setParameter("qty", data.getQty())
                .setParameter("medical_facility_id", data.getMedicalFacilityId())
                .setParameter("courir_id", data.getCourirId())
                .setParameter("sub_total", data.getSubTotal())
                .setParameter("created_by", data.getCreatedBy())
                .setParameter("created_on", data.getCreatedOn())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .executeUpdate();
    }

    public void update(TMedicalItemPurchaseDetail data) {
        String sql = "UPDATE " + TABLE
                + " SET medical_item_purchase_id = :medical_item_purchase_id, medical_item_id = :medical_item_id, qty = :qty, medical_facility_id = :medical_facility_id, courir_id = :courir_id, sub_total = :sub_total, modified_by = :modified_by, modified_on = :modified_on, deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("medical_item_purchase_id", data.getMedicalItemPurchaseId())
                .setParameter("medical_item_id", data.getMedicalItemId())
                .setParameter("qty", data.getQty())
                .setParameter("medical_facility_id", data.getMedicalFacilityId())
                .setParameter("courir_id", data.getCourirId())
                .setParameter("sub_total", data.getSubTotal())
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
            case "medicalItemPurchaseId" -> "medical_item_purchase_id";
            case "medicalItemId" -> "medical_item_id";
            case "medicalFacilityId" -> "medical_facility_id";
            case "courirId" -> "courir_id";
            case "subTotal" -> "sub_total";
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
