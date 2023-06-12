package com.dodoDev.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "UserToken")
public class UsersTokens {
    @Id
    private String token;
    @ManyToOne
    private Users user;
}
