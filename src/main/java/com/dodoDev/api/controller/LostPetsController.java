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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LostPetsController {

    @Autowired
    private LostPetsRepository repository;
    @Autowired
    private PetBreedRepository petBreedRepository;
    @Autowired
    private SexRepository sexRepository;
    @Autowired
    private LastLocationRepository lastLocationRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private PetForAdoptionRepository petForAdoptionRepository;
    @Autowired
    private DiscardedReportsRepository discardedReportsRepository;

    @GetMapping("/lostPets")
    public ResponseEntity<Object> allLostPets(@RequestHeader("Authorization") String token) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/GetPosiblePet/{id}")
    public ResponseEntity<Object> getAllPosibleDog(

            @PathVariable("id") int lostPet_id
    ){
        LostPets lostPets;
        List<Integer> pet = new ArrayList<>();
        if(repository.existsById(lostPet_id)) {
            lostPets = repository.findById(lostPet_id).get();
            lostPets.getDiscardedReports().forEach(e -> pet.add(e.getId()));

            return new ResponseEntity<>(petForAdoptionRepository.findAllExceptDiscardedReports(pet, lostPets.getPetBreed()), HttpStatus.OK);
        }
        return ResponseEntityToken.TokenError("No se encuentra registrada la mascota perdida que se ingreso", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/DiscardPosiblePet/{id}")
    public ResponseEntity<Object> discardPosiblePet(
            @PathVariable("id") int lostPet_id,
            @RequestParam("petExcluded_id") int petExcluded_id
    ) {
        if(petForAdoptionRepository.existsById(petExcluded_id) && repository.existsById(lostPet_id)) {
            PetForAdoption petForAdoption = petForAdoptionRepository.findById(petExcluded_id).get();
            LostPets lostPets = repository.findById(lostPet_id).get();
            DiscardedReports discardedReports = new DiscardedReports(petForAdoption);

            discardedReportsRepository.save(discardedReports);
            lostPets.getDiscardedReports().add(discardedReports);
            return new ResponseEntity<>(repository.save(lostPets), HttpStatus.OK);
        }

        return ResponseEntityToken.TokenError("Id de mascota perdida o id de mascota en adopcion en error", HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/UpdateLostPet/{id}")
    public ResponseEntity<Object> updateLostPet(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int id,
            @RequestParam("name")           Optional<String> name,
            @RequestParam("id_sex")         Optional<Integer> idsex,
            @RequestParam("age")            Optional<Integer> age,
            @RequestParam("id_petBreed")    Optional<Integer> idPetBreed,
            @RequestParam("size")           Optional<String> size,
            @RequestParam("description")    Optional<String> description,
            @RequestParam("image")          Optional<MultipartFile> image,
            @RequestParam("location_latitude")   Optional<String> location_latitude,
            @RequestParam("location_altitude")   Optional<String> location_altitude

    ) throws IOException {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        if(!repository.existsById(id)) return ResponseEntityToken.TokenError("Mascota no encontrada", HttpStatus.NOT_FOUND);
        LostPets lostPets = repository.findById(id).get();

        if(idsex.isPresent()) {
            if(!sexRepository.existsById(idsex.get())) return ResponseEntityToken.TokenError("Id del sexo invalido", HttpStatus.NOT_ACCEPTABLE);
            lostPets.setSex(sexRepository.findById(idsex.get()).get());
        }
        if(idPetBreed.isPresent()) {
            if(!petBreedRepository.existsById(idPetBreed.get())) return ResponseEntityToken.TokenError("Id de la raza invalido", HttpStatus.NOT_ACCEPTABLE);
            lostPets.setPetBreed(petBreedRepository.findById(idPetBreed.get()).get());
        }
        if(image.isPresent()) {
            lostPets.setImagePath(MyPetSOSUtil.saveImage(image.get()));
        }

        name.ifPresent(lostPets::setName);
        age.ifPresent(lostPets::setAge);
        size.ifPresent(lostPets::setSize);
        description.ifPresent(lostPets::setDescription);
        location_altitude.ifPresent(lostPets.getLocations()::setAltitude);
        location_latitude.ifPresent(lostPets.getLocations()::setLatitude);

        return new ResponseEntity<>(repository.save(lostPets), HttpStatus.OK) ;
    }

    @DeleteMapping("/DeleteLostPet/{id}")
    public ResponseEntity<Object> deleteLostPet(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") int id
    ) {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        if(!repository.existsById(id)) return ResponseEntityToken.TokenError("Mascota no encontrada", HttpStatus.NOT_FOUND);

        repository.deleteById(id);
        return ResponseEntityToken.TokenError("Mascota eliminada de los registros", HttpStatus.OK);
    }

    @PostMapping("/lostPets")
    public ResponseEntity<Object> postLostPets(
            @RequestHeader("Authorization") String token,
            @RequestParam("name")           String name,
            @RequestParam("id_sex")         int idsex,
            @RequestParam("age")            int age,
            @RequestParam("id_petBreed")    int idPetBreed,
            @RequestParam("size")           String size,
            @RequestParam("description")    String description,
            @RequestParam("image")          Optional<MultipartFile> image,
            @RequestParam("location_latitude")   String location_latitude,
            @RequestParam("location_altitude")   String location_altitude
    ) throws IOException {
        if(!userTokenRepository.existsById(token)) return ResponseEntityToken.TokenError("Token invalido", HttpStatus.UNAUTHORIZED);
        UsersTokens usersTokens = userTokenRepository.findById(token).get();
        Users user = usersTokens.getUser();

        Optional<PetBreed> petBreedSelected = petBreedRepository.findById(idPetBreed);
        Optional<Sex> sexSelected = sexRepository.findById(idsex);

        PetBreed petBreed = null;
        Sex sex = null;
        if(petBreedSelected.isPresent() && sexSelected.isPresent()) {
            petBreed = petBreedSelected.get();
            sex = sexSelected.get();
        } else {
           return ResponseEntityToken.TokenError("Datos no validos.", HttpStatus.NOT_ACCEPTABLE);
        }

        LostPets newLostPet = new LostPets(name, sex, age, petBreed, size, description);
        newLostPet.setLocations(lastLocationRepository.save(new LastLocations(location_latitude, location_altitude)));
        newLostPet.setUser(user);
        if(image.isPresent()) {
            newLostPet.setImagePath(MyPetSOSUtil.saveImage(image.get()));
        }
        return new ResponseEntity<>(repository.save(newLostPet), HttpStatus.OK);
    }
}
