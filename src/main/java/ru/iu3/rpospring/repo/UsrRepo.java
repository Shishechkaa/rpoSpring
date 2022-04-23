package ru.iu3.rpospring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.rpospring.domain.Usr;

public interface UsrRepo extends JpaRepository<Usr, Long> {
}
