package io.github.amsatrio.modules.hospital.t_appointment;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "t_appointment")
public class TAppointment {

    @Id
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    @JsonProperty("customerId")
    @Column(name = "customer_id", columnDefinition = "bigint")
    private Long customerId;
    @JsonProperty("doctorOfficeId")
    @Column(name = "doctor_office_id", columnDefinition = "bigint")
    private Long doctorOfficeId;
    @JsonProperty("doctorOfficeScheduleId")
    @Column(name = "doctor_office_schedule_id", columnDefinition = "bigint")
    private Long doctorOfficeScheduleId;
    @JsonProperty("doctorOfficeTreatmentId")
    @Column(name = "doctor_office_treatment_id", columnDefinition = "bigint")
    private Long doctorOfficeTreatmentId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("appointmentDate")
    @Column(name = "appointment_date", columnDefinition = "date")
    private Date appointmentDate;
    @NotNull(message = "created_by is mandatory")
    @JsonProperty("createdBy")
    @Column(name = "created_by", columnDefinition = "bigint")
    private Long createdBy;
    @NotNull(message = "created_on is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("createdOn")
    @Column(name = "created_on", columnDefinition = "datetime")
    private Date createdOn;
    @JsonProperty("modifiedBy")
    @Column(name = "modified_by", columnDefinition = "bigint")
    private Long modifiedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("modifiedOn")
    @Column(name = "modified_on", columnDefinition = "datetime")
    private Date modifiedOn;
    @JsonProperty("deletedBy")
    @Column(name = "deleted_by", columnDefinition = "bigint")
    private Long deletedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("deletedOn")
    @Column(name = "deleted_on", columnDefinition = "datetime")
    private Date deletedOn;
    @NotNull(message = "is_delete is mandatory")
    @JsonProperty("isDelete")
    @Column(name = "is_delete", columnDefinition = "boolean comment 'default FALSE'")
    private Boolean isDelete = false;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public Long getDoctorOfficeId() {
        return doctorOfficeId;
    }
    public void setDoctorOfficeId(Long doctorOfficeId) {
        this.doctorOfficeId = doctorOfficeId;
    }
    public Long getDoctorOfficeScheduleId() {
        return doctorOfficeScheduleId;
    }
    public void setDoctorOfficeScheduleId(Long doctorOfficeScheduleId) {
        this.doctorOfficeScheduleId = doctorOfficeScheduleId;
    }
    public Long getDoctorOfficeTreatmentId() {
        return doctorOfficeTreatmentId;
    }
    public void setDoctorOfficeTreatmentId(Long doctorOfficeTreatmentId) {
        this.doctorOfficeTreatmentId = doctorOfficeTreatmentId;
    }
    public Date getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public Long getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    public Date getCreatedOn() {
        return createdOn;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    public Long getModifiedBy() {
        return modifiedBy;
    }
    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    public Date getModifiedOn() {
        return modifiedOn;
    }
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
    public Long getDeletedBy() {
        return deletedBy;
    }
    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }
    public Date getDeletedOn() {
        return deletedOn;
    }
    public void setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
    }
    public Boolean getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
