package com.dodoDev.api.repository;

import com.dodoDev.api.entity.LostPets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LostPetsRepository extends JpaRepository<LostPets, Integer> {
    @Override
    List<LostPets> findAll();

    void deleteById(int id);
}
