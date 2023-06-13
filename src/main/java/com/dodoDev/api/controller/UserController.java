package com.dodoDev.api.controller;

import com.dodoDev.api.ResponseEntityToken;
import com.dodoDev.api.MyPetSOSUtil;
import com.dodoDev.api.entity.Users;
import com.dodoDev.api.entity.UsersTokens;
import com.dodoDev.api.repository.UserRepository;
import com.dodoDev.api.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTokenRepository userTokenRepository;


    @GetMapping("/users")
    ResponseEntity<Object> getAllUser(@RequestHeader("Authorization") String token) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/closeAllSession")
    ResponseEntity<Object> closeAllSession(@RequestHeader("Authorization") String token) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        Users user = userTokenRepository.findById(token).get().getUser();

        userTokenRepository.deleteUsersTokensByUser(user);
        return ResponseEntityToken.TokenError("Sesiones Cerradas", HttpStatus.OK);
    }

    @PostMapping("/signin")
    ResponseEntity<Object> signIn(@RequestParam String email, @RequestParam String password) {
        Users user = userRepository.findByEmail(email);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(user == null || !passwordEncoder.matches(password, user.getPassword()))
            return  ResponseEntityToken.TokenError("Correo o contraseña son incorrectas. Verifique e intente de nuevo", HttpStatus.UNAUTHORIZED);

        Map<String, Object> respuesta = new HashMap<>();
        UsersTokens newToken = new UsersTokens();
        newToken.setUser(user);
        newToken.setToken(MyPetSOSUtil.generateToken());

        userTokenRepository.save(newToken);
        respuesta.put("name", user.getName());
        respuesta.put("role_id", user.getRole_id());
        respuesta.put("surname", user.getSurname());
        respuesta.put("email", user.getEmail());
        respuesta.put("phone", user.getPhone());
        respuesta.put("curp", user.getCurp());
        respuesta.put("image", user.getImagePath());
        respuesta.put("token", newToken.getToken());

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @GetMapping("/validSession")
    ResponseEntity<Object> validSession(
            @RequestHeader("Authorization") String token
    ) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        Users user = userTokenRepository.findById(token).get().getUser();

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/signup")
    ResponseEntity<Object> postNewUser(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("curp") String curp,
            @RequestParam("password") String password,
            @RequestParam("image") Optional<MultipartFile> image
            ) throws IOException {
        if(userRepository.existsByEmail(email) || userRepository.existsByCurp(curp))
            return  ResponseEntityToken.TokenError("Este correo electronico o CURP ya fue registrado.", HttpStatus.BAD_REQUEST);

        Map<String, Object> respuesta = new HashMap<>();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users newUser = new Users(name, surname, email, phone, curp,2);
        newUser.setPassword(passwordEncoder.encode(password));

        if(image.isPresent()) {
            newUser.setImagePath(MyPetSOSUtil.saveImage(image.get()));
        }

        UsersTokens usersTokens = new UsersTokens();
        usersTokens.setUser(newUser);
        usersTokens.setToken(MyPetSOSUtil.generateToken());

        userRepository.save(newUser);
        userTokenRepository.save(usersTokens);

        respuesta.put("name", newUser.getName());
        respuesta.put("surname", newUser.getSurname());
        respuesta.put("email", newUser.getEmail());
        respuesta.put("role_id", newUser.getRole_id());
        respuesta.put("phone", newUser.getPhone());
        respuesta.put("curp", newUser.getCurp());
        respuesta.put("image", newUser.getImagePath());
        respuesta.put("token", usersTokens.getToken());

        usersTokens.setToken(passwordEncoder.encode(usersTokens.getToken()));
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
