package io.github.amsatrio.modules.hospital.m_medical_item;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MMedicalItemRepository implements PanacheRepository<MMedicalItem> {

    private static final String TABLE = "m_medical_item";

    private static final String SELECT_ALL = "SELECT id, name, medical_item_category_id, composition, medical_item_segmentation_id, manufacturer, indication, dosage, directions, contraindication, caution, packaging, price_max, price_min, image, image_path, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM m_medical_item";

    public MMedicalItem findByIdCached(Long id) {
        return (MMedicalItem) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", MMedicalItem.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<MMedicalItem> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, MMedicalItem.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<MMedicalItem> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, MMedicalItem.class);
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

    public void insert(MMedicalItem data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, name, medical_item_category_id, composition, medical_item_segmentation_id, manufacturer, indication, dosage, directions, contraindication, caution, packaging, price_max, price_min, image, image_path, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :name, :medical_item_category_id, :composition, :medical_item_segmentation_id, :manufacturer, :indication, :dosage, :directions, :contraindication, :caution, :packaging, :price_max, :price_min, :image, :image_path, :created_by, :created_on, :modified_by, :modified_on, :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("name", data.getName())
                .setParameter("medical_item_category_id", data.getMedicalItemCategoryId())
                .setParameter("composition", data.getComposition())
                .setParameter("medical_item_segmentation_id", data.getMedicalItemSegmentationId())
                .setParameter("manufacturer", data.getManufacturer())
                .setParameter("indication", data.getIndication())
                .setParameter("dosage", data.getDosage())
                .setParameter("directions", data.getDirections())
                .setParameter("contraindication", data.getContraindication())
                .setParameter("caution", data.getCaution())
                .setParameter("packaging", data.getPackaging())
                .setParameter("price_max", data.getPriceMax())
                .setParameter("price_min", data.getPriceMin())
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

    public void update(MMedicalItem data) {
        String sql = "UPDATE " + TABLE
                + " SET name = :name, medical_item_category_id = :medical_item_category_id, composition = :composition, medical_item_segmentation_id = :medical_item_segmentation_id, manufacturer = :manufacturer, indication = :indication, dosage = :dosage, directions = :directions, contraindication = :contraindication, caution = :caution, packaging = :packaging, price_max = :price_max, price_min = :price_min, image = :image, image_path = :image_path, modified_by = :modified_by, modified_on = :modified_on, deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("name", data.getName())
                .setParameter("medical_item_category_id", data.getMedicalItemCategoryId())
                .setParameter("composition", data.getComposition())
                .setParameter("medical_item_segmentation_id", data.getMedicalItemSegmentationId())
                .setParameter("manufacturer", data.getManufacturer())
                .setParameter("indication", data.getIndication())
                .setParameter("dosage", data.getDosage())
                .setParameter("directions", data.getDirections())
                .setParameter("contraindication", data.getContraindication())
                .setParameter("caution", data.getCaution())
                .setParameter("packaging", data.getPackaging())
                .setParameter("price_max", data.getPriceMax())
                .setParameter("price_min", data.getPriceMin())
                .setParameter("image", data.getImage())
                .setParameter("image_path", data.getImagePath())
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
            case "medicalItemCategoryId" -> "medical_item_category_id";
            case "medicalItemSegmentationId" -> "medical_item_segmentation_id";
            case "priceMax" -> "price_max";
            case "priceMin" -> "price_min";
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
