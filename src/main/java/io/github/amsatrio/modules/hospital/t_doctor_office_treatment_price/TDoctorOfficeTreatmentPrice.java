package io.github.amsatrio.modules.hospital.t_doctor_office_treatment_price;

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
@Table(name = "t_doctor_office_treatment_price")
public class TDoctorOfficeTreatmentPrice {

    @Id
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    @JsonProperty("doctorOfficeTreatmentId")
    @Column(name = "doctor_office_treatment_id", columnDefinition = "bigint")
    private Long doctorOfficeTreatmentId;
    @JsonProperty("price")
    @Column(name = "price", columnDefinition = "decimal(10,0)")
    private BigDecimal price;
    @JsonProperty("priceStartFrom")
    @Column(name = "price_start_from", columnDefinition = "decimal(10,0)")
    private BigDecimal priceStartFrom;
    @JsonProperty("priceUntilFrom")
    @Column(name = "price_until_from", columnDefinition = "decimal(10,0)")
    private BigDecimal priceUntilFrom;
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
    public Long getDoctorOfficeTreatmentId() {
        return doctorOfficeTreatmentId;
    }
    public void setDoctorOfficeTreatmentId(Long doctorOfficeTreatmentId) {
        this.doctorOfficeTreatmentId = doctorOfficeTreatmentId;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public BigDecimal getPriceStartFrom() {
        return priceStartFrom;
    }
    public void setPriceStartFrom(BigDecimal priceStartFrom) {
        this.priceStartFrom = priceStartFrom;
    }
    public BigDecimal getPriceUntilFrom() {
        return priceUntilFrom;
    }
    public void setPriceUntilFrom(BigDecimal priceUntilFrom) {
        this.priceUntilFrom = priceUntilFrom;
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
