package com.dodoDev.api.repository;

import com.dodoDev.api.entity.PetForAdoption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PetRepository extends JpaRepository<PetForAdoption, Long> {
    @Override
    List<PetForAdoption> findAll();

}
