package ru.iu3.rpospring.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iu3.rpospring.domain.Artist;
import ru.iu3.rpospring.domain.Country;
import ru.iu3.rpospring.repo.ArtistRepo;
import ru.iu3.rpospring.repo.CountryRepo;
import ru.iu3.rpospring.tools.DataValidationException;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
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
    public Page<Country> getAllCountries(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return countryRepo.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity getCountry(@PathVariable(value = "id") Long countryId)
        throws DataValidationException {
        Country country = countryRepo.findById(countryId).
                orElseThrow(()->new DataValidationException("Страна с таким индексом не найдена"));
        return ResponseEntity.ok(country);
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
    public ResponseEntity<Country> updateCountry(@PathVariable(value = "id") Long countryId,
                                                 @Valid @RequestBody Country countryDetails)
            throws DataValidationException {
        try {
            Country country = countryRepo.findById(countryId)
                    .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
            country.setName(countryDetails.getName());
            countryRepo.save(country);
            return ResponseEntity.ok(country);
        }
        catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PostMapping("/deletecountries")
    public ResponseEntity deleteCountries(@Valid @RequestBody List<Country> countries) {
        countryRepo.deleteAll(countries);
        return new ResponseEntity(HttpStatus.OK);
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
