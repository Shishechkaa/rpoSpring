package ru.iu3.rpospring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.rpospring.domain.Artist;

public interface ArtistRepo extends JpaRepository<Artist, Long> {
}
