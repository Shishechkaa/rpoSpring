package ru.iu3.rpospring.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.rpospring.domain.Country;
import ru.iu3.rpospring.repo.CountryRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CountryController {
    final CountryRepo countryRepo;

    public CountryController(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        return countryRepo.findAll();
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
        Optional<Country> countryFromDB = countryRepo.findById(id);
        if (countryFromDB.isPresent()) {
            BeanUtils.copyProperties(countryFromDB, country, "id");
            countryRepo.save(countryFromDB.get());
            return ResponseEntity.ok(countryFromDB.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "country not found");
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
}
