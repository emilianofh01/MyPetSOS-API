package com.dodoDev.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "LostPets")
public class LostPets {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    @ManyToOne
    private Sex sex;
    @ManyToOne
    private PetBreed petBreed;
    private String size;
    private int age;
    private String description;
    @ManyToOne
    private Users user;
    @OneToOne
    private LastLocations locations;

    @ManyToMany
    private List<DiscardedReports> discardedReports;

    private String imagePath;

    public LostPets(String name, Sex sex, int age, PetBreed petBreed, String size, String description) {
        this.name = name;
        this.sex = sex;
        this.petBreed = petBreed;
        this.size = size;
        this.description = description;
        this.age = age;
    }

    @JsonIgnore
    public List<DiscardedReports> getDiscardedReports() {
        return discardedReports;
    }
}
