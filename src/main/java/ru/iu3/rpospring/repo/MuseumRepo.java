package ru.iu3.rpospring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.rpospring.domain.Museum;

public interface MuseumRepo extends JpaRepository<Museum, Long> {
}
