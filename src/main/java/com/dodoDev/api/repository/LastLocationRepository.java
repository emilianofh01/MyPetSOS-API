package com.dodoDev.api.repository;

import com.dodoDev.api.entity.LastLocations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LastLocationRepository extends JpaRepository<LastLocations, Integer> {
    @Override
    List<LastLocations> findAll();
}
