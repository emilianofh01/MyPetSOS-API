package com.dodoDev.api.repository;

import com.dodoDev.api.entity.Vaccines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaccinesRepository extends JpaRepository<Vaccines, Integer> {

    List<Vaccines> findAllByIdIn(List<Integer> id);
}
