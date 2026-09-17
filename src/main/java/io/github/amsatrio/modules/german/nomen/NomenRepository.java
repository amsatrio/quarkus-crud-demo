package io.github.amsatrio.modules.german.nomen;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NomenRepository implements PanacheRepository<Nomen> {

    private static final String TABLE = "nouns";
    private static final String JOIN_TABLE = "noun_categories";

    private static final String SELECT_ALL = "SELECT id, singular, gender, plural, genitive_singular, "
            + "is_n_deklination, translation_en, example_sentence_de, example_sentence_en, "
            + "level, created_at, deleted_at FROM " + TABLE;

    private static final String NON_DELETED = " deleted_at IS NULL ";

    public Nomen findById(Long id) {
        Nomen nomen = (Nomen) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id AND" + NON_DELETED + "LIMIT 1", Nomen.class)
                .setParameter("id", id)
                .getSingleResult();
        nomen.setCategories(findCategoryIds(id));
        return nomen;
    }

    public List<Nomen> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " WHERE" + NON_DELETED
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        return getEntityManager()
                .createNativeQuery(sql, Nomen.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize)
                .getResultList();
    }

    public long countAll() {
        return ((Number) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE + " WHERE" + NON_DELETED)
                .getSingleResult()).longValue();
    }

    public List<Nomen> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE" + NON_DELETED + "AND " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager().createNativeQuery(sql, Nomen.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setParameter("limit", pageSize);
        query.setParameter("offset", pageIndex * pageSize);
        return query.getResultList();
    }

    public long countByFilter(String whereClause, Map<String, Object> params) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE" + NON_DELETED + "AND " + whereClause;
        var query = getEntityManager().createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return ((Number) query.getSingleResult()).longValue();
    }

    public List<Nomen> search(String keyword, int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " WHERE" + NON_DELETED
                + " AND (singular LIKE :kw OR plural LIKE :kw OR genitive_singular LIKE :kw"
                + " OR translation_en LIKE :kw OR example_sentence_de LIKE :kw OR example_sentence_en LIKE :kw)"
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        return getEntityManager()
                .createNativeQuery(sql, Nomen.class)
                .setParameter("kw", "%" + keyword + "%")
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize)
                .getResultList();
    }

    public long countBySearch(String keyword) {
        String sql = "SELECT COUNT(*) FROM " + TABLE
                + " WHERE" + NON_DELETED
                + " AND (singular LIKE :kw OR plural LIKE :kw OR genitive_singular LIKE :kw"
                + " OR translation_en LIKE :kw OR example_sentence_de LIKE :kw OR example_sentence_en LIKE :kw)";
        return ((Number) getEntityManager()
                .createNativeQuery(sql)
                .setParameter("kw", "%" + keyword + "%")
                .getSingleResult()).longValue();
    }

    public Long insert(Nomen data) {
        getEntityManager().createNativeQuery(
                "INSERT INTO " + TABLE
                + " (singular, gender, plural, genitive_singular, is_n_deklination,"
                + " translation_en, example_sentence_de, example_sentence_en, level, created_at, deleted_at)"
                + " VALUES (:singular, :gender, :plural, :genitive_singular, :is_n_deklination,"
                + " :translation_en, :example_sentence_de, :example_sentence_en, :level, :created_at, :deleted_at)")
                .setParameter("singular", data.getSingular())
                .setParameter("gender", data.getGender())
                .setParameter("plural", data.getPlural())
                .setParameter("genitive_singular", data.getGenitiveSingular())
                .setParameter("is_n_deklination", data.getIsNDeklination())
                .setParameter("translation_en", data.getTranslationEn())
                .setParameter("example_sentence_de", data.getExampleSentenceDe())
                .setParameter("example_sentence_en", data.getExampleSentenceEn())
                .setParameter("level", data.getLevel())
                .setParameter("created_at", data.getCreatedAt())
                .setParameter("deleted_at", data.getDeletedAt())
                .executeUpdate();

        Long id = ((Number) getEntityManager()
                .createNativeQuery("SELECT LAST_INSERT_ID()")
                .getSingleResult()).longValue();
        replaceCategories(id, data.getCategories());
        return id;
    }

    public void update(Nomen data) {
        getEntityManager().createNativeQuery(
                "UPDATE " + TABLE
                + " SET singular = :singular, gender = :gender, plural = :plural,"
                + " genitive_singular = :genitive_singular, is_n_deklination = :is_n_deklination,"
                + " translation_en = :translation_en,"
                + " example_sentence_de = :example_sentence_de, example_sentence_en = :example_sentence_en,"
                + " level = :level"
                + " WHERE id = :id AND" + NON_DELETED)
                .setParameter("id", data.getId())
                .setParameter("singular", data.getSingular())
                .setParameter("gender", data.getGender())
                .setParameter("plural", data.getPlural())
                .setParameter("genitive_singular", data.getGenitiveSingular())
                .setParameter("is_n_deklination", data.getIsNDeklination())
                .setParameter("translation_en", data.getTranslationEn())
                .setParameter("example_sentence_de", data.getExampleSentenceDe())
                .setParameter("example_sentence_en", data.getExampleSentenceEn())
                .setParameter("level", data.getLevel())
                .executeUpdate();
        replaceCategories(data.getId(), data.getCategories());
    }

    public void softDelete(Long id) {
        getEntityManager().createNativeQuery(
                "UPDATE " + TABLE + " SET deleted_at = :deletedAt WHERE id = :id AND" + NON_DELETED)
                .setParameter("id", id)
                .setParameter("deletedAt", new java.util.Date())
                .executeUpdate();
    }

    public void hardDelete(Long id) {
        getEntityManager().createNativeQuery("DELETE FROM " + JOIN_TABLE + " WHERE noun_id = :id")
                .setParameter("id", id)
                .executeUpdate();
        getEntityManager().createNativeQuery("DELETE FROM " + TABLE + " WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    private List<Long> findCategoryIds(Long nounId) {
        List<?> raw = getEntityManager()
                .createNativeQuery("SELECT category_id FROM " + JOIN_TABLE + " WHERE noun_id = :nounId ORDER BY category_id")
                .setParameter("nounId", nounId)
                .getResultList();
        return raw.stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
    }

    private void replaceCategories(Long nounId, List<Long> categoryIds) {
        getEntityManager().createNativeQuery("DELETE FROM " + JOIN_TABLE + " WHERE noun_id = :nounId")
                .setParameter("nounId", nounId)
                .executeUpdate();
        if (categoryIds != null) {
            for (Long categoryId : categoryIds) {
                getEntityManager().createNativeQuery(
                        "INSERT IGNORE INTO " + JOIN_TABLE + " (noun_id, category_id) VALUES (:nounId, :categoryId)")
                        .setParameter("nounId", nounId)
                        .setParameter("categoryId", categoryId)
                        .executeUpdate();
            }
        }
    }

    public static String toColumnName(String fieldName) {
        return switch (fieldName) {
            case "genitiveSingular" -> "genitive_singular";
            case "isNDeklination" -> "is_n_deklination";
            case "translationEn" -> "translation_en";
            case "exampleSentenceDe" -> "example_sentence_de";
            case "exampleSentenceEn" -> "example_sentence_en";
            case "createdAt" -> "created_at";
            case "deletedAt" -> "deleted_at";
            default -> fieldName;
        };
    }
}