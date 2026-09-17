package io.github.amsatrio.modules.hospital.m_doctor_education;

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
@Table(name = "m_doctor_education")
public class MDoctorEducation {

    @Id
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    @JsonProperty("doctorId")
    @Column(name = "doctor_id", columnDefinition = "bigint")
    private Long doctorId;
    @JsonProperty("educationLevelId")
    @Column(name = "education_level_id", columnDefinition = "bigint")
    private Long educationLevelId;
    @Length(max = 100, message = "institutionName must be between 0-100 characters")
    @JsonProperty("institutionName")
    @Column(name = "institution_name", columnDefinition = "varchar(100)")
    private String institutionName;
    @Length(max = 100, message = "major must be between 0-100 characters")
    @JsonProperty("major")
    @Column(name = "major", columnDefinition = "varchar(100)")
    private String major;
    @Length(max = 4, message = "startYear must be between 0-4 characters")
    @JsonProperty("startYear")
    @Column(name = "start_year", columnDefinition = "varchar(4)")
    private String startYear;
    @Length(max = 4, message = "endYear must be between 0-4 characters")
    @JsonProperty("endYear")
    @Column(name = "end_year", columnDefinition = "varchar(4)")
    private String endYear;
    @JsonProperty("isLastEducation")
    @Column(name = "is_last_education", columnDefinition = "tinyint(1)")
    private Boolean isLastEducation;
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
    public Long getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    public Long getEducationLevelId() {
        return educationLevelId;
    }
    public void setEducationLevelId(Long educationLevelId) {
        this.educationLevelId = educationLevelId;
    }
    public String getInstitutionName() {
        return institutionName;
    }
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public String getStartYear() {
        return startYear;
    }
    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }
    public String getEndYear() {
        return endYear;
    }
    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }
    public Boolean getIsLastEducation() {
        return isLastEducation;
    }
    public void setIsLastEducation(Boolean isLastEducation) {
        this.isLastEducation = isLastEducation;
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
