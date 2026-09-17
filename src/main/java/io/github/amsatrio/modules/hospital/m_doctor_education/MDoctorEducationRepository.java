package io.github.amsatrio.modules.hospital.m_doctor_education;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MDoctorEducationRepository implements PanacheRepository<MDoctorEducation> {

    private static final String TABLE = "m_doctor_education";

    private static final String SELECT_ALL = "SELECT id, doctor_id, education_level_id, institution_name, major, start_year, end_year, is_last_education, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM m_doctor_education";

    public MDoctorEducation findById(Long id) {
        return (MDoctorEducation) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", MDoctorEducation.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<MDoctorEducation> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, MDoctorEducation.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<MDoctorEducation> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, MDoctorEducation.class);
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

    public void insert(MDoctorEducation data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, doctor_id, education_level_id, institution_name, major, start_year, end_year, is_last_education, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :doctor_id, :education_level_id, :institution_name, :major, :start_year, :end_year, :is_last_education, :created_by, :created_on, :modified_by, :modified_on, :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("doctor_id", data.getDoctorId())
                .setParameter("education_level_id", data.getEducationLevelId())
                .setParameter("institution_name", data.getInstitutionName())
                .setParameter("major", data.getMajor())
                .setParameter("start_year", data.getStartYear())
                .setParameter("end_year", data.getEndYear())
                .setParameter("is_last_education", data.getIsLastEducation())
                .setParameter("created_by", data.getCreatedBy())
                .setParameter("created_on", data.getCreatedOn())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .executeUpdate();
    }

    public void update(MDoctorEducation data) {
        String sql = "UPDATE " + TABLE
                + " SET doctor_id = :doctor_id, education_level_id = :education_level_id, institution_name = :institution_name, major = :major, start_year = :start_year, end_year = :end_year, is_last_education = :is_last_education, modified_by = :modified_by, modified_on = :modified_on, deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("doctor_id", data.getDoctorId())
                .setParameter("education_level_id", data.getEducationLevelId())
                .setParameter("institution_name", data.getInstitutionName())
                .setParameter("major", data.getMajor())
                .setParameter("start_year", data.getStartYear())
                .setParameter("end_year", data.getEndYear())
                .setParameter("is_last_education", data.getIsLastEducation())
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
            case "doctorId" -> "doctor_id";
            case "educationLevelId" -> "education_level_id";
            case "institutionName" -> "institution_name";
            case "startYear" -> "start_year";
            case "endYear" -> "end_year";
            case "isLastEducation" -> "is_last_education";
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
