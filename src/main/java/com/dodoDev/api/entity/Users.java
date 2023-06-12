package com.dodoDev.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int role_id;
    @Column(nullable = false)
    private String name, surname, email, phone, password;

    @Column(nullable = false)
    private String curp;

    private String imagePath;

    public Users(String name, String surname, String email, String phone, String curp,int role_id) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.curp = curp;
        this.role_id = role_id;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
