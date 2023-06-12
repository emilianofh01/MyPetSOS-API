package com.dodoDev.api.repository;

import com.dodoDev.api.entity.Sex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SexRepository extends JpaRepository<Sex, Integer> {
    @Override
    List<Sex> findAll();
}
