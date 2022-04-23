package ru.iu3.rpospring.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.rpospring.domain.Museum;
import ru.iu3.rpospring.domain.Usr;
import ru.iu3.rpospring.repo.MuseumRepo;
import ru.iu3.rpospring.repo.UsrRepo;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/users")
public class UsrController {

    private final UsrRepo usrRepo;
    private final MuseumRepo museumRepo;

    public UsrController(UsrRepo usrRepo, MuseumRepo museumRepo) {
        this.usrRepo = usrRepo;
        this.museumRepo = museumRepo;
    }

    @GetMapping
    public List<Usr> getAllUsers () {
        return usrRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> createUser(
            @RequestBody Usr user
    ) throws Exception {
        try {
            Usr nu = usrRepo.save(user);
            return ResponseEntity.ok(nu);
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

    @PostMapping("/{id}/addmuseums")
    public ResponseEntity<Object> addMuseums(
            @PathVariable(value = "id") Long userId,
            @Valid @RequestBody Set<Museum> museumSet) {
        Optional<Usr> uu = usrRepo.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            Usr usr = uu.get();
            for (Museum m : museumSet) {
                Optional<Museum> mm = museumRepo.findById(m.getId());
                if (mm.isPresent()) {
                    usr.addMuseum(mm.get());
                    cnt++;
                }
            }
            usrRepo.save(usr);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/removemuseums")
    public ResponseEntity<Object> removeMuseums(
            @PathVariable(value = "id") Long usrID,
            @Valid @RequestBody Set<Museum> museums
    ) {
        Optional<Usr> uu = usrRepo.findById(usrID);
        int cnt = 0;
        if (uu.isPresent()) {
            Usr u = uu.get();
            for (Museum m : u.museums) {
                u.removeMuseum(m);
                cnt++;
            }
            usrRepo.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usr> updateUser(
            @PathVariable(value = "id") Long userId,
            @RequestBody Usr usrDetails) {

        Optional<Usr> uu = usrRepo.findById(userId);
        if (uu.isPresent()) {
            Usr userFromDB = uu.get();
            BeanUtils.copyProperties(usrDetails, userFromDB,
                    "id", "token", "activity", "password", "salt");
            usrRepo.save(userFromDB);
            return ResponseEntity.ok(userFromDB);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
    }
}
