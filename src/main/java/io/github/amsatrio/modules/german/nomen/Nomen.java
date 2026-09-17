package io.github.amsatrio.modules.german.nomen;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "nouns")
public class Nomen {

    @Id
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "int")
    private Long id;

    @NotNull(message = "singular is mandatory")
    @Length(max = 100, message = "singular must be between 0-100 characters")
    @JsonProperty("singular")
    @Column(name = "singular", columnDefinition = "varchar(100)")
    private String singular;

    @NotNull(message = "gender is mandatory")
    @JsonProperty("gender")
    @Column(name = "gender", columnDefinition = "varchar(20)")
    private String gender;

    @Length(max = 100, message = "plural must be between 0-100 characters")
    @JsonProperty("plural")
    @Column(name = "plural", columnDefinition = "varchar(100)")
    private String plural;

    @Length(max = 100, message = "genitive_singular must be between 0-100 characters")
    @JsonProperty("genitiveSingular")
    @Column(name = "genitive_singular", columnDefinition = "varchar(100)")
    private String genitiveSingular;

    @JsonProperty("isNDeklination")
    @Column(name = "is_n_deklination", columnDefinition = "tinyint(1)")
    private Boolean isNDeklination = false;

    @NotNull(message = "translation_en is mandatory")
    @Length(max = 255, message = "translation_en must be between 0-255 characters")
    @JsonProperty("translationEn")
    @Column(name = "translation_en", columnDefinition = "varchar(255)")
    private String translationEn;

    @JsonProperty("exampleSentenceDe")
    @Column(name = "example_sentence_de", columnDefinition = "text")
    private String exampleSentenceDe;

    @JsonProperty("exampleSentenceEn")
    @Column(name = "example_sentence_en", columnDefinition = "text")
    private String exampleSentenceEn;

    @NotNull(message = "level is mandatory")
    @JsonProperty("level")
    @Column(name = "level", columnDefinition = "varchar(10)")
    private String level = "A1";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("createdAt")
    @Column(name = "created_at", columnDefinition = "datetime")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("deletedAt")
    @Column(name = "deleted_at", columnDefinition = "datetime")
    private Date deletedAt;

    @Transient
    @JsonProperty("categories")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSingular() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public String getGenitiveSingular() {
        return genitiveSingular;
    }

    public void setGenitiveSingular(String genitiveSingular) {
        this.genitiveSingular = genitiveSingular;
    }

    public Boolean getIsNDeklination() {
        return isNDeklination;
    }

    public void setIsNDeklination(Boolean isNDeklination) {
        this.isNDeklination = isNDeklination;
    }

    public String getTranslationEn() {
        return translationEn;
    }

    public void setTranslationEn(String translationEn) {
        this.translationEn = translationEn;
    }

    public String getExampleSentenceDe() {
        return exampleSentenceDe;
    }

    public void setExampleSentenceDe(String exampleSentenceDe) {
        this.exampleSentenceDe = exampleSentenceDe;
    }

    public String getExampleSentenceEn() {
        return exampleSentenceEn;
    }

    public void setExampleSentenceEn(String exampleSentenceEn) {
        this.exampleSentenceEn = exampleSentenceEn;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }
}