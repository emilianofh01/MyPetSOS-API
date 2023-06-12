package com.dodoDev.api.controller;

import com.dodoDev.api.ResponseEntityToken;
import com.dodoDev.api.entity.Sex;
import com.dodoDev.api.repository.SexRepository;
import com.dodoDev.api.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SexController {
    @Autowired
    private SexRepository repository;
    @Autowired
    private UserTokenRepository userTokenRepository;

    @GetMapping("/sex")
    public ResponseEntity<Object> allSexOption(@RequestHeader("Authorization") String token) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/sex")
    public ResponseEntity<Object> postSex(@RequestHeader("Authorization") String token, @RequestParam String name) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);

        Sex newSex = new Sex();
        newSex.setName(name);
        return new ResponseEntity<>(repository.save(newSex), HttpStatus.OK);
    }
}
