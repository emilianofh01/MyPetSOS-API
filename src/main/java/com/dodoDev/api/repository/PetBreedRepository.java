package com.dodoDev.api.repository;

import com.dodoDev.api.entity.PetBreed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetBreedRepository extends JpaRepository<PetBreed, Integer> {
    @Override
    List<PetBreed> findAll();
}
