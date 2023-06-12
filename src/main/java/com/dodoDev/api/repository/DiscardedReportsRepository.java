package com.dodoDev.api.repository;

import com.dodoDev.api.entity.DiscardedReports;
import com.dodoDev.api.entity.PetBreed;
import com.dodoDev.api.entity.PetForAdoption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscardedReportsRepository extends JpaRepository<DiscardedReports, Integer> {
}
