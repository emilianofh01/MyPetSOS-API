package com.dodoDev.api.repository;

import com.dodoDev.api.entity.DiscardedReports;
import com.dodoDev.api.entity.LostPets;
import com.dodoDev.api.entity.PetBreed;
import com.dodoDev.api.entity.PetForAdoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface PetForAdoptionRepository extends JpaRepository<PetForAdoption, Integer> {
    @Query("SELECT pet FROM PetForAdoption pet WHERE pet.petBreed = :petBreed AND pet NOT IN (SELECT dr.mascota FROM DiscardedReports dr WHERE dr.id IN :discardedReportIds)")
    List<PetForAdoption> findAllExceptDiscardedReports(@Param("discardedReportIds") List<Integer> discardedReportIds, @Param("petBreed") PetBreed petBreed);
}
