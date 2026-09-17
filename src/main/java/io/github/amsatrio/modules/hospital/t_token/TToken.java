package io.github.amsatrio.modules.hospital.t_token;

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
@Table(name = "t_token")
public class TToken {

    @Id
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    @Length(max = 100, message = "email must be between 0-100 characters")
    @JsonProperty("email")
    @Column(name = "email", columnDefinition = "varchar(100)")
    private String email;
    @JsonProperty("userId")
    @Column(name = "user_id", columnDefinition = "bigint")
    private Long userId;
    @Length(max = 50, message = "token must be between 0-50 characters")
    @JsonProperty("token")
    @Column(name = "token", columnDefinition = "varchar(50)")
    private String token;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("expiredOn")
    @Column(name = "expired_on", columnDefinition = "datetime")
    private Date expiredOn;
    @JsonProperty("isExpired")
    @Column(name = "is_expired", columnDefinition = "tinyint(1)")
    private Boolean isExpired;
    @Length(max = 20, message = "usedFor must be between 0-20 characters")
    @JsonProperty("usedFor")
    @Column(name = "used_for", columnDefinition = "varchar(20)")
    private String usedFor;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Date getExpiredOn() {
        return expiredOn;
    }
    public void setExpiredOn(Date expiredOn) {
        this.expiredOn = expiredOn;
    }
    public Boolean getIsExpired() {
        return isExpired;
    }
    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }
    public String getUsedFor() {
        return usedFor;
    }
    public void setUsedFor(String usedFor) {
        this.usedFor = usedFor;
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
