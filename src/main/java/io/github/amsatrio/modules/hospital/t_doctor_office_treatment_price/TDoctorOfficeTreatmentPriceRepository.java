package io.github.amsatrio.modules.hospital.t_doctor_office_treatment_price;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TDoctorOfficeTreatmentPriceRepository implements PanacheRepository<TDoctorOfficeTreatmentPrice> {

    private static final String TABLE = "t_doctor_office_treatment_price";

    private static final String SELECT_ALL = "SELECT id, doctor_office_treatment_id, price, price_start_from, price_until_from, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM t_doctor_office_treatment_price";

    public TDoctorOfficeTreatmentPrice findByIdCached(Long id) {
        return (TDoctorOfficeTreatmentPrice) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", TDoctorOfficeTreatmentPrice.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<TDoctorOfficeTreatmentPrice> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, TDoctorOfficeTreatmentPrice.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<TDoctorOfficeTreatmentPrice> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, TDoctorOfficeTreatmentPrice.class);
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

    public void insert(TDoctorOfficeTreatmentPrice data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, doctor_office_treatment_id, price, price_start_from, price_until_from, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :doctor_office_treatment_id, :price, :price_start_from, :price_until_from, :created_by, :created_on, :modified_by, :modified_on, :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("doctor_office_treatment_id", data.getDoctorOfficeTreatmentId())
                .setParameter("price", data.getPrice())
                .setParameter("price_start_from", data.getPriceStartFrom())
                .setParameter("price_until_from", data.getPriceUntilFrom())
                .setParameter("created_by", data.getCreatedBy())
                .setParameter("created_on", data.getCreatedOn())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .executeUpdate();
    }

    public void update(TDoctorOfficeTreatmentPrice data) {
        String sql = "UPDATE " + TABLE
                + " SET doctor_office_treatment_id = :doctor_office_treatment_id, price = :price, price_start_from = :price_start_from, price_until_from = :price_until_from, modified_by = :modified_by, modified_on = :modified_on, deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("doctor_office_treatment_id", data.getDoctorOfficeTreatmentId())
                .setParameter("price", data.getPrice())
                .setParameter("price_start_from", data.getPriceStartFrom())
                .setParameter("price_until_from", data.getPriceUntilFrom())
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
            case "doctorOfficeTreatmentId" -> "doctor_office_treatment_id";
            case "priceStartFrom" -> "price_start_from";
            case "priceUntilFrom" -> "price_until_from";
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
