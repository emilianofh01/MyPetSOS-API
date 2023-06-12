package com.dodoDev.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "Veterinary")
public class Veterinary {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String number;
    private String emergencyNumber;
    private String doctor;
    private String direccion;
    //private Blob image;

    @OneToOne
    private VeterinaryLocations location;
}
