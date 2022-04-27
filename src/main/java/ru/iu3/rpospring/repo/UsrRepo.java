package ru.iu3.rpospring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.rpospring.domain.Usr;

import java.util.Optional;

public interface UsrRepo extends JpaRepository<Usr, Long> {
    Optional<Usr> findByToken(String token);
    Optional<Usr> findByLogin(String login);
}
