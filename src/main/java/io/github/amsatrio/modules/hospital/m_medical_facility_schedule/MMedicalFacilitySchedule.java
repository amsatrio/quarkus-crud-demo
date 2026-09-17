package io.github.amsatrio.modules.hospital.m_medical_facility_schedule;

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
@Table(name = "m_medical_facility_schedule")
public class MMedicalFacilitySchedule {

    @Id
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    @JsonProperty("medicalFacilityId")
    @Column(name = "medical_facility_id", columnDefinition = "bigint")
    private Long medicalFacilityId;
    @Length(max = 10, message = "day must be between 0-10 characters")
    @JsonProperty("day")
    @Column(name = "day", columnDefinition = "varchar(10)")
    private String day;
    @Length(max = 10, message = "timeScheduleStart must be between 0-10 characters")
    @JsonProperty("timeScheduleStart")
    @Column(name = "time_schedule_start", columnDefinition = "varchar(10)")
    private String timeScheduleStart;
    @Length(max = 10, message = "timeScheduleEnd must be between 0-10 characters")
    @JsonProperty("timeScheduleEnd")
    @Column(name = "time_schedule_end", columnDefinition = "varchar(10)")
    private String timeScheduleEnd;
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
    public Long getMedicalFacilityId() {
        return medicalFacilityId;
    }
    public void setMedicalFacilityId(Long medicalFacilityId) {
        this.medicalFacilityId = medicalFacilityId;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getTimeScheduleStart() {
        return timeScheduleStart;
    }
    public void setTimeScheduleStart(String timeScheduleStart) {
        this.timeScheduleStart = timeScheduleStart;
    }
    public String getTimeScheduleEnd() {
        return timeScheduleEnd;
    }
    public void setTimeScheduleEnd(String timeScheduleEnd) {
        this.timeScheduleEnd = timeScheduleEnd;
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
