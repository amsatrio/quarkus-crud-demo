package io.github.amsatrio.modules.hospital.m_medical_facility;

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
@Table(name = "m_medical_facility")
public class MMedicalFacility {

    @Id
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    @Length(max = 50, message = "name must be between 0-50 characters")
    @JsonProperty("name")
    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;
    @JsonProperty("medicalFacilityCategoryId")
    @Column(name = "medical_facility_category_id", columnDefinition = "bigint")
    private Long medicalFacilityCategoryId;
    @JsonProperty("locationId")
    @Column(name = "location_id", columnDefinition = "bigint")
    private Long locationId;
    @JsonProperty("fullAddress")
    @Column(name = "full_address", columnDefinition = "text")
    private String fullAddress;
    @Length(max = 100, message = "email must be between 0-100 characters")
    @JsonProperty("email")
    @Column(name = "email", columnDefinition = "varchar(100)")
    private String email;
    @Length(max = 10, message = "phoneCode must be between 0-10 characters")
    @JsonProperty("phoneCode")
    @Column(name = "phone_code", columnDefinition = "varchar(10)")
    private String phoneCode;
    @Length(max = 15, message = "phone must be between 0-15 characters")
    @JsonProperty("phone")
    @Column(name = "phone", columnDefinition = "varchar(15)")
    private String phone;
    @Length(max = 15, message = "fax must be between 0-15 characters")
    @JsonProperty("fax")
    @Column(name = "fax", columnDefinition = "varchar(15)")
    private String fax;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getMedicalFacilityCategoryId() {
        return medicalFacilityCategoryId;
    }
    public void setMedicalFacilityCategoryId(Long medicalFacilityCategoryId) {
        this.medicalFacilityCategoryId = medicalFacilityCategoryId;
    }
    public Long getLocationId() {
        return locationId;
    }
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
    public String getFullAddress() {
        return fullAddress;
    }
    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneCode() {
        return phoneCode;
    }
    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
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
