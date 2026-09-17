package io.github.amsatrio.modules.hospital.m_biodata_address;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MBiodataAddressRepository implements PanacheRepository<MBiodataAddress> {

    private static final String TABLE = "m_biodata_address";

    private static final String SELECT_ALL = "SELECT id, biodata_id, label, recipient, recipient_phone_number, location_id, postal_code, address, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM m_biodata_address";

    public MBiodataAddress findById(Long id) {
        return (MBiodataAddress) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", MBiodataAddress.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<MBiodataAddress> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, MBiodataAddress.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<MBiodataAddress> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, MBiodataAddress.class);
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

    public void insert(MBiodataAddress data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, biodata_id, label, recipient, recipient_phone_number, location_id, postal_code, address, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :biodata_id, :label, :recipient, :recipient_phone_number, :location_id, :postal_code, :address, :created_by, :created_on, :modified_by, :modified_on, :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("biodata_id", data.getBiodataId())
                .setParameter("label", data.getLabel())
                .setParameter("recipient", data.getRecipient())
                .setParameter("recipient_phone_number", data.getRecipientPhoneNumber())
                .setParameter("location_id", data.getLocationId())
                .setParameter("postal_code", data.getPostalCode())
                .setParameter("address", data.getAddress())
                .setParameter("created_by", data.getCreatedBy())
                .setParameter("created_on", data.getCreatedOn())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .executeUpdate();
    }

    public void update(MBiodataAddress data) {
        String sql = "UPDATE " + TABLE
                + " SET biodata_id = :biodata_id, label = :label, recipient = :recipient, recipient_phone_number = :recipient_phone_number, location_id = :location_id, postal_code = :postal_code, address = :address, modified_by = :modified_by, modified_on = :modified_on, deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("biodata_id", data.getBiodataId())
                .setParameter("label", data.getLabel())
                .setParameter("recipient", data.getRecipient())
                .setParameter("recipient_phone_number", data.getRecipientPhoneNumber())
                .setParameter("location_id", data.getLocationId())
                .setParameter("postal_code", data.getPostalCode())
                .setParameter("address", data.getAddress())
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
            case "recipientPhoneNumber" -> "recipient_phone_number";
            case "locationId" -> "location_id";
            case "postalCode" -> "postal_code";
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
