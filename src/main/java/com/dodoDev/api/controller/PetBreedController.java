package com.dodoDev.api.controller;

import com.dodoDev.api.ResponseEntityToken;
import com.dodoDev.api.entity.PetBreed;
import com.dodoDev.api.repository.PetBreedRepository;
import com.dodoDev.api.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PetBreedController {

    @Autowired
    public PetBreedRepository repository;
    @Autowired
    public UserTokenRepository userTokenRepository;

    @GetMapping("/petBreed")
    public ResponseEntity<Object> getAllPetBreed(@RequestHeader("Authorization") String token) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/petBreed")
    public ResponseEntity<Object> addNewPetBreed(@RequestHeader("Authorization") String token, @RequestParam("name") String name) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);

        PetBreed newPetBreed = new PetBreed();
        newPetBreed.setName(name);

        return new ResponseEntity<>(repository.save(newPetBreed), HttpStatus.OK);
    }
}
