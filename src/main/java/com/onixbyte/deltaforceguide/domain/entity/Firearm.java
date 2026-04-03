package com.onixbyte.deltaforceguide.domain.entity;

import com.onixbyte.deltaforceguide.domain.converter.FirearmTypeConverter;
import com.onixbyte.deltaforceguide.enumeration.FirearmType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "firearm")
public class Firearm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "type", nullable = false)
    @Convert(converter = FirearmTypeConverter.class)
    private FirearmType type;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "review", columnDefinition = "TEXT")
    private String review;

    @OneToMany(mappedBy = "firearm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Modification> modifications = new ArrayList<>();

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

    public FirearmType getType() {
        return type;
    }

    public void setType(FirearmType type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public List<Modification> getModifications() {
        return modifications;
    }

    public void setModifications(List<Modification> modifications) {
        this.modifications = modifications;
    }

    public void addModification(Modification modification) {
        this.modifications.add(modification);
        modification.setFirearm(this);
    }

    public void removeModification(Modification modification) {
        this.modifications.remove(modification);
        modification.setFirearm(null);
    }
}

