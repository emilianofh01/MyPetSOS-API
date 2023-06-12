package com.dodoDev.api.controller;

import com.dodoDev.api.MyPetSOSUtil;
import com.dodoDev.api.ResponseEntityToken;
import com.dodoDev.api.entity.*;
import com.dodoDev.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class PetForAdoptionController {

    @Autowired
    private PetForAdoptionRepository petForAdoptionRepository;
    @Autowired
    private PetBreedRepository petBreedRepository;
    @Autowired
    private SexRepository sexRepository;
    @Autowired
    private VaccinesRepository vaccinesRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;

    @GetMapping("/PetForAdoption")
    public ResponseEntity<Object> getAllPetForAdoption(@RequestHeader("Authorization") String token) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(petForAdoptionRepository.findAll(), HttpStatus.OK);
    }

    @PutMapping("/UpdatePetForAdoption/{id}")
    public ResponseEntity<Object> updatePetForAdoption(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int id,
            @RequestParam("name")           Optional<String> name,
            @RequestParam("id_sex")         Optional<Integer> idsex,
            @RequestParam("age")            Optional<Integer> age,
            @RequestParam("id_petBreed")    Optional<Integer> idPetBreed,
            @RequestParam("vaccines")       Optional<List<Integer>> id_vaccines,
            @RequestParam("size")           Optional<String> size,
            @RequestParam("image")          Optional<MultipartFile> image,

            @RequestParam("description")    Optional<String> description
    ) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        if(!petForAdoptionRepository.existsById(id)) return ResponseEntityToken.TokenError("Mascota no encontrada", HttpStatus.NOT_FOUND);
        PetForAdoption petForAdoption = petForAdoptionRepository.findById(id).get();

        if(idsex.isPresent()) {
            if(!sexRepository.existsById(idsex.get())) return ResponseEntityToken.TokenError("Id del sexo invalido", HttpStatus.NOT_ACCEPTABLE);
            petForAdoption.setSex(sexRepository.findById(idsex.get()).get());
        }
        if(idPetBreed.isPresent()) {
            if(!petBreedRepository.existsById(idPetBreed.get())) return ResponseEntityToken.TokenError("Id de la raza invalido", HttpStatus.NOT_ACCEPTABLE);
            petForAdoption.setPetBreed(petBreedRepository.findById(idPetBreed.get()).get());
        }

        name.ifPresent(petForAdoption::setName);
        age.ifPresent(petForAdoption::setAge);
        size.ifPresent(petForAdoption::setSize);
        image.ifPresent(e -> {
            try {
                petForAdoption.setImagePath(MyPetSOSUtil.saveImage(e));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        description.ifPresent(petForAdoption::setDescription);
        id_vaccines.ifPresent(vaccine -> {
            petForAdoption.setVaccines(vaccinesRepository.findAllByIdIn(id_vaccines.get()));
        });

        return new ResponseEntity<>(petForAdoptionRepository.save(petForAdoption), HttpStatus.OK) ;
    }

    @DeleteMapping("/DeletePetForAdoption/{id}")
    public ResponseEntity<Object> deletePetForAdoption(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int id
    ) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        if(!petForAdoptionRepository.existsById(id)) return ResponseEntityToken.TokenError("Mascota no encontrada", HttpStatus.NOT_FOUND);

        petForAdoptionRepository.deleteById(id);
        return ResponseEntityToken.TokenError("Mascota eliminada de los registros", HttpStatus.OK);
    }

    @PostMapping("/petForAdoption")
    public ResponseEntity<Object> postPetForAdoption(
            @RequestHeader("Authorization") String token,
            @RequestParam("name")           String name,
            @RequestParam("id_sex")         int idsex,
            @RequestParam("age")            int age,
            @RequestParam("id_petBreed")    int idPetBreed,
            @RequestParam("vaccines")       List<Integer> id_vaccines,
            @RequestParam("size")           String size,
            @RequestParam("description")    String description,

            @RequestParam("image") MultipartFile image
    ) throws IOException {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);

        Optional<PetBreed> petBreedSelected = petBreedRepository.findById(idPetBreed);
        Optional<Sex> sexSelected = sexRepository.findById(idsex);

        PetBreed petBreed = null;
        Sex sex = null;
        List<Vaccines> vaccinesList = vaccinesRepository.findAllByIdIn(id_vaccines);

        if(petBreedSelected.isPresent() && sexSelected.isPresent()) {
            petBreed = petBreedSelected.get();
            sex = sexSelected.get();
        } else {
           return ResponseEntityToken.TokenError("Datos no validos.", HttpStatus.NOT_ACCEPTABLE);
        }

        PetForAdoption newPetForAdoption = new PetForAdoption(name, sex, age, petBreed, size, description, vaccinesList);
        newPetForAdoption.setImagePath(MyPetSOSUtil.saveImage(image));
        return new ResponseEntity<>(petForAdoptionRepository.save(newPetForAdoption), HttpStatus.OK);
    }
}
