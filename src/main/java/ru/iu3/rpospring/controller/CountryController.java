package ru.iu3.rpospring.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.rpospring.domain.Artist;
import ru.iu3.rpospring.domain.Country;
import ru.iu3.rpospring.repo.ArtistRepo;
import ru.iu3.rpospring.repo.CountryRepo;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class CountryController {
    final CountryRepo countryRepo;
    final ArtistRepo artistRepo;

    public CountryController(CountryRepo countryRepo, ArtistRepo artistRepo) {
        this.countryRepo = countryRepo;
        this.artistRepo = artistRepo;
    }

    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        return countryRepo.findAll();
    }

    @GetMapping("/countries/{id}/artists")
    public ResponseEntity<List<Artist>> getCountryArtists(
            @PathVariable(value = "id") Long countryId
    ) {
        Optional<Country> cc = countryRepo.findById(countryId);
        if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get().artists);
        }

        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(
            @RequestBody Country country
    ) throws Exception {
        try {
            Country nc = countryRepo.save(country);
            return ResponseEntity.ok(nc);
        } catch (Exception ex) {
            String error;
            if (ex.getCause().getCause().getMessage().contains("повторяющееся значение ключа"))
                error = "countryalreadyexists";
            else
                error = "undefinderror";
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<Country> updateCountry(
            @PathVariable(value = "id") Long id,
            @RequestBody Country country
            ) {
        Optional<Country> uc = countryRepo.findById(id);
        if (uc.isPresent()) {
            Country countryFromDB = uc.get();
            BeanUtils.copyProperties(country, countryFromDB, "id");
            countryRepo.save(countryFromDB);
            return ResponseEntity.ok(countryFromDB);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "country not found");
        }

    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable(value = "id") Long id) {
        Optional<Country> country = countryRepo.findById(id);
        Map<String, Boolean> resp = new HashMap<>();
        if (country.isPresent()) {
            countryRepo.delete(country.get());
            resp.put("deleted", Boolean.TRUE);
        } else {
            resp.put("deleted", Boolean.FALSE);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(
            @RequestBody Artist artist
            ) throws Exception {
        try {
            Optional<Country>
                    cc = countryRepo.findById(artist.getCountry().getId());
            if (cc.isPresent()) {
                artist.setCountry(cc.get());
            }
            Artist nc = artistRepo.save(artist);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(ex, HttpStatus.NOT_FOUND);
        }
    }
}
