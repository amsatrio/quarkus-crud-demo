package io.github.amsatrio.modules.hospital.t_appointment;

import java.util.List;
import java.util.Map;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TAppointmentRepository implements PanacheRepository<TAppointment> {

    private static final String TABLE = "t_appointment";

    private static final String SELECT_ALL = "SELECT id, customer_id, doctor_office_id, doctor_office_schedule_id, doctor_office_treatment_id, appointment_date, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete FROM t_appointment";

    public TAppointment findById(Long id) {
        return (TAppointment) getEntityManager()
                .createNativeQuery(SELECT_ALL + " WHERE id = :id LIMIT 1", TAppointment.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<TAppointment> findAll(int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";

        var query = getEntityManager()
                .createNativeQuery(sql, TAppointment.class)
                .setParameter("limit", pageSize)
                .setParameter("offset", pageIndex * pageSize);

        return query.getResultList();
    }

    public long countAll() {
        return (Long) getEntityManager()
                .createNativeQuery("SELECT COUNT(*) FROM " + TABLE)
                .getSingleResult();
    }

    public List<TAppointment> findByFilter(String whereClause, Map<String, Object> params,
            int pageIndex, int pageSize, String sortColumn, boolean sortAsc) {
        String sql = SELECT_ALL + " WHERE " + whereClause
                + " ORDER BY " + sortColumn + (sortAsc ? " ASC" : " DESC")
                + " LIMIT :limit OFFSET :offset";
        var query = getEntityManager()
                .createNativeQuery(sql, TAppointment.class);
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

    public void insert(TAppointment data) {
        String sql = "INSERT INTO " + TABLE
                + " (id, customer_id, doctor_office_id, doctor_office_schedule_id, doctor_office_treatment_id, appointment_date, created_by, created_on, modified_by, modified_on, deleted_by, deleted_on, is_delete)"
                + " VALUES"
                + " (:id, :customer_id, :doctor_office_id, :doctor_office_schedule_id, :doctor_office_treatment_id, :appointment_date, :created_by, :created_on, :modified_by, :modified_on, :deleted_by, :deleted_on, :is_delete)";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("id", data.getId())
                .setParameter("customer_id", data.getCustomerId())
                .setParameter("doctor_office_id", data.getDoctorOfficeId())
                .setParameter("doctor_office_schedule_id", data.getDoctorOfficeScheduleId())
                .setParameter("doctor_office_treatment_id", data.getDoctorOfficeTreatmentId())
                .setParameter("appointment_date", data.getAppointmentDate())
                .setParameter("created_by", data.getCreatedBy())
                .setParameter("created_on", data.getCreatedOn())
                .setParameter("modified_by", data.getModifiedBy())
                .setParameter("modified_on", data.getModifiedOn())
                .setParameter("deleted_by", data.getDeletedBy())
                .setParameter("deleted_on", data.getDeletedOn())
                .setParameter("is_delete", data.getIsDelete())
                .executeUpdate();
    }

    public void update(TAppointment data) {
        String sql = "UPDATE " + TABLE
                + " SET customer_id = :customer_id, doctor_office_id = :doctor_office_id, doctor_office_schedule_id = :doctor_office_schedule_id, doctor_office_treatment_id = :doctor_office_treatment_id, appointment_date = :appointment_date, modified_by = :modified_by, modified_on = :modified_on, deleted_by = :deleted_by, deleted_on = :deleted_on, is_delete = :is_delete"
                + " WHERE id = :id";
        getEntityManager()
                .createNativeQuery(sql)
                .setParameter("customer_id", data.getCustomerId())
                .setParameter("doctor_office_id", data.getDoctorOfficeId())
                .setParameter("doctor_office_schedule_id", data.getDoctorOfficeScheduleId())
                .setParameter("doctor_office_treatment_id", data.getDoctorOfficeTreatmentId())
                .setParameter("appointment_date", data.getAppointmentDate())
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
            case "customerId" -> "customer_id";
            case "doctorOfficeId" -> "doctor_office_id";
            case "doctorOfficeScheduleId" -> "doctor_office_schedule_id";
            case "doctorOfficeTreatmentId" -> "doctor_office_treatment_id";
            case "appointmentDate" -> "appointment_date";
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
