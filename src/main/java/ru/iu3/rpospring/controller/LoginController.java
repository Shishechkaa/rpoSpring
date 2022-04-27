package ru.iu3.rpospring.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iu3.rpospring.domain.Usr;
import ru.iu3.rpospring.repo.UsrRepo;
import ru.iu3.rpospring.tools.Utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class LoginController {

    final private UsrRepo usrRepo;

    public LoginController(UsrRepo usrRepo) {
        this.usrRepo = usrRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String pwd = credentials.get("password");
        if (!pwd.isEmpty() && !login.isEmpty()) {
            Optional<Usr> uu = usrRepo.findByLogin(login);
            if (uu.isPresent()) {
                Usr u2 = uu.get();
                String hash1 = u2.getPassword();
                String salt = u2.getSalt();
                String hash2 = Utils.ComputeHash(pwd, salt);
                if (hash1.equalsIgnoreCase(hash2)) {
                    String token = UUID.randomUUID().toString();
                    u2.setToken(token);
                    u2.setActivity(LocalDateTime.now());
                    Usr u3 = usrRepo.saveAndFlush(u2);
                    return new ResponseEntity<>(u3, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && !token.isEmpty()) {
            token = StringUtils.removeStart(token, "Bearer").trim();
            Optional uu = usrRepo.findByToken(token);
            if (uu.isPresent()) {
                Usr u = (Usr) uu.get();
                u.setToken(null);
                usrRepo.save(u);
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
