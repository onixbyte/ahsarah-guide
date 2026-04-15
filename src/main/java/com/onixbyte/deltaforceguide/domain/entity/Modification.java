package com.onixbyte.deltaforceguide.domain.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "modification",
    indexes = {
        @Index(name = "idx_modification_firearm_id", columnList = "firearm_id")
    }
)
public class Modification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "firearm_id", nullable = false, foreignKey = @ForeignKey(name = "fk_modification_firearm"))
    private Firearm firearm;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "code", nullable = false, length = 64)
    private String code;

    @Type(JsonType.class)
    @Column(name = "tags", columnDefinition = "json")
    private List<String> tags = new ArrayList<>();

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "author", length = 64)
    private String author;

    @Column(name = "video_url", length = 512)
    private String videoUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Firearm getFirearm() {
        return firearm;
    }

    public void setFirearm(Firearm firearm) {
        this.firearm = firearm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private Firearm firearm;
        private String name;
        private String code;
        private List<String> tags;
        private String note;
        private String author;
        private String videoUrl;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firearm(Firearm firearm) {
            this.firearm = firearm;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder videoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public Modification build() {
            Modification modification = new Modification();
            modification.id = this.id;
            modification.firearm = this.firearm;
            modification.name = this.name;
            modification.code = this.code;
            modification.tags = this.tags == null ? new ArrayList<>() : this.tags;
            modification.note = this.note;
            modification.author = this.author;
            modification.videoUrl = this.videoUrl;
            return modification;
        }
    }
}

