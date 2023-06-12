package com.dodoDev.api.repository;

import com.dodoDev.api.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByCurp(String curp);
}
