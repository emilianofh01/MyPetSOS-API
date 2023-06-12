package com.dodoDev.api.repository;

import com.dodoDev.api.entity.Users;
import com.dodoDev.api.entity.UsersTokens;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTokenRepository extends JpaRepository<UsersTokens, String> {
    @Override
    List<UsersTokens> findAll();

    @Transactional
    void deleteUsersTokensByUser(Users user);
}
