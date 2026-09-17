package io.github.amsatrio.modules.hospital.m_user;

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
@Table(name = "m_user")
public class MUser {

    @Id
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    @JsonProperty("biodataId")
    @Column(name = "biodata_id", columnDefinition = "bigint")
    private Long biodataId;
    @JsonProperty("roleId")
    @Column(name = "role_id", columnDefinition = "bigint")
    private Long roleId;
    @Length(max = 100, message = "email must be between 0-100 characters")
    @JsonProperty("email")
    @Column(name = "email", columnDefinition = "varchar(100)")
    private String email;
    @Length(max = 255, message = "password must be between 0-255 characters")
    @JsonProperty("password")
    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;
    @JsonProperty("loginAttempt")
    @Column(name = "login_attempt", columnDefinition = "int")
    private Integer loginAttempt;
    @JsonProperty("isLocked")
    @Column(name = "is_locked", columnDefinition = "tinyint(1)")
    private Boolean isLocked;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("lastLogin")
    @Column(name = "last_login", columnDefinition = "datetime")
    private Date lastLogin;
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
    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getLoginAttempt() {
        return loginAttempt;
    }
    public void setLoginAttempt(Integer loginAttempt) {
        this.loginAttempt = loginAttempt;
    }
    public Boolean getIsLocked() {
        return isLocked;
    }
    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }
    public Date getLastLogin() {
        return lastLogin;
    }
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
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
