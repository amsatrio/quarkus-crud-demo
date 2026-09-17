package io.github.amsatrio.modules.hospital.m_customer;

import java.util.Date;
import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "m_customer")
public class MCustomer {

    @Id
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    @JsonProperty("biodataId")
    @Column(name = "biodata_id", columnDefinition = "bigint")
    private Long biodataId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("dob")
    @Column(name = "dob", columnDefinition = "date")
    private Date dob;
    @Length(max = 1, message = "gender must be between 0-1 characters")
    @JsonProperty("gender")
    @Column(name = "gender", columnDefinition = "varchar(1)")
    private String gender;
    @JsonProperty("bloodGroupId")
    @Column(name = "blood_group_id", columnDefinition = "bigint")
    private Long bloodGroupId;
    @Length(max = 5, message = "rhesusType must be between 0-5 characters")
    @JsonProperty("rhesusType")
    @Column(name = "rhesus_type", columnDefinition = "varchar(5)")
    private String rhesusType;
    @JsonProperty("height")
    @Column(name = "height", columnDefinition = "decimal(10,0)")
    private BigDecimal height;
    @JsonProperty("weight")
    @Column(name = "weight", columnDefinition = "decimal(10,0)")
    private BigDecimal weight;
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
    public Long getBiodataId() {
        return biodataId;
    }
    public void setBiodataId(Long biodataId) {
        this.biodataId = biodataId;
    }
    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public Long getBloodGroupId() {
        return bloodGroupId;
    }
    public void setBloodGroupId(Long bloodGroupId) {
        this.bloodGroupId = bloodGroupId;
    }
    public String getRhesusType() {
        return rhesusType;
    }
    public void setRhesusType(String rhesusType) {
        this.rhesusType = rhesusType;
    }
    public BigDecimal getHeight() {
        return height;
    }
    public void setHeight(BigDecimal height) {
        this.height = height;
    }
    public BigDecimal getWeight() {
        return weight;
    }
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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
