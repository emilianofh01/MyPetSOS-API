package com.dodoDev.api.controller;

import com.dodoDev.api.ResponseEntityToken;
import com.dodoDev.api.entity.Vaccines;
import com.dodoDev.api.repository.UserTokenRepository;
import com.dodoDev.api.repository.VaccinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5500")
@RequestMapping("/api")
public class VaccinesController {
    @Autowired
    VaccinesRepository vaccinesRepository;
    @Autowired
    UserTokenRepository userTokenRepository;

    @PostMapping("/Vaccines")
    ResponseEntity<Object> addVaccines(
            @RequestHeader("Authorization") String token,
            @RequestParam("name") String name
    ) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        Vaccines newVaccines = new Vaccines(name);

        return new ResponseEntity<>(vaccinesRepository.save(newVaccines), HttpStatus.OK);
    }

    @GetMapping("/Vaccines")
    ResponseEntity<Object> getVaccines(
            @RequestHeader("Authorization") String token
    ) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(vaccinesRepository.findAll(), HttpStatus.OK);
    }
}
