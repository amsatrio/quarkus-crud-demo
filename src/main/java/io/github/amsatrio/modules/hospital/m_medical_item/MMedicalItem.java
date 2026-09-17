package io.github.amsatrio.modules.hospital.m_medical_item;

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
@Table(name = "m_medical_item")
public class MMedicalItem {

    @Id
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    @Length(max = 50, message = "name must be between 0-50 characters")
    @JsonProperty("name")
    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;
    @JsonProperty("medicalItemCategoryId")
    @Column(name = "medical_item_category_id", columnDefinition = "bigint")
    private Long medicalItemCategoryId;
    @JsonProperty("composition")
    @Column(name = "composition", columnDefinition = "text")
    private String composition;
    @JsonProperty("medicalItemSegmentationId")
    @Column(name = "medical_item_segmentation_id", columnDefinition = "bigint")
    private Long medicalItemSegmentationId;
    @Length(max = 100, message = "manufacturer must be between 0-100 characters")
    @JsonProperty("manufacturer")
    @Column(name = "manufacturer", columnDefinition = "varchar(100)")
    private String manufacturer;
    @JsonProperty("indication")
    @Column(name = "indication", columnDefinition = "text")
    private String indication;
    @JsonProperty("dosage")
    @Column(name = "dosage", columnDefinition = "text")
    private String dosage;
    @JsonProperty("directions")
    @Column(name = "directions", columnDefinition = "text")
    private String directions;
    @JsonProperty("contraindication")
    @Column(name = "contraindication", columnDefinition = "text")
    private String contraindication;
    @JsonProperty("caution")
    @Column(name = "caution", columnDefinition = "text")
    private String caution;
    @Length(max = 50, message = "packaging must be between 0-50 characters")
    @JsonProperty("packaging")
    @Column(name = "packaging", columnDefinition = "varchar(50)")
    private String packaging;
    @JsonProperty("priceMax")
    @Column(name = "price_max", columnDefinition = "bigint")
    private Long priceMax;
    @JsonProperty("priceMin")
    @Column(name = "price_min", columnDefinition = "bigint")
    private Long priceMin;
    @JsonProperty("image")
    @Column(name = "image", columnDefinition = "mediumblob")
    private byte[] image;
    @Length(max = 100, message = "imagePath must be between 0-100 characters")
    @JsonProperty("imagePath")
    @Column(name = "image_path", columnDefinition = "varchar(100)")
    private String imagePath;
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
    public Long getMedicalItemCategoryId() {
        return medicalItemCategoryId;
    }
    public void setMedicalItemCategoryId(Long medicalItemCategoryId) {
        this.medicalItemCategoryId = medicalItemCategoryId;
    }
    public String getComposition() {
        return composition;
    }
    public void setComposition(String composition) {
        this.composition = composition;
    }
    public Long getMedicalItemSegmentationId() {
        return medicalItemSegmentationId;
    }
    public void setMedicalItemSegmentationId(Long medicalItemSegmentationId) {
        this.medicalItemSegmentationId = medicalItemSegmentationId;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getIndication() {
        return indication;
    }
    public void setIndication(String indication) {
        this.indication = indication;
    }
    public String getDosage() {
        return dosage;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    public String getDirections() {
        return directions;
    }
    public void setDirections(String directions) {
        this.directions = directions;
    }
    public String getContraindication() {
        return contraindication;
    }
    public void setContraindication(String contraindication) {
        this.contraindication = contraindication;
    }
    public String getCaution() {
        return caution;
    }
    public void setCaution(String caution) {
        this.caution = caution;
    }
    public String getPackaging() {
        return packaging;
    }
    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }
    public Long getPriceMax() {
        return priceMax;
    }
    public void setPriceMax(Long priceMax) {
        this.priceMax = priceMax;
    }
    public Long getPriceMin() {
        return priceMin;
    }
    public void setPriceMin(Long priceMin) {
        this.priceMin = priceMin;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
