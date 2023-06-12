package com.dodoDev.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "petForAdoption")
public class PetForAdoption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int age;
    private String size;

    @ManyToOne
    private PetBreed petBreed;
    @ManyToOne
    private Sex sex;
    @Value("")
    private String description;

    @ManyToMany
    private List<Vaccines> vaccines;

    private String imagePath;

    @CreationTimestamp
    private LocalDateTime dateRegister;

    public PetForAdoption(String name, Sex sex, int age, PetBreed petBreed, String size, String description, List<Vaccines> vaccines) {
        this.name = name;
        this.age = age;
        this.size = size;
        this.petBreed = petBreed;
        this.sex = sex;
        this.description = description;
        this.vaccines = vaccines;
    }
}
