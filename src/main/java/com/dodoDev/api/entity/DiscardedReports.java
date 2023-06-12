package com.dodoDev.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "DiscardedReports")
public class DiscardedReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private PetForAdoption mascota;

    public DiscardedReports(PetForAdoption mascota) {
        this.mascota = mascota;
    }
}
