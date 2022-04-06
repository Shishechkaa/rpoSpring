package ru.iu3.rpospring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iu3.rpospring.domain.Country;

public interface CountryRepo extends JpaRepository<Country, Long> {

}
