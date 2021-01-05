package ru.mine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mine.domain.Driver;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

    List<Driver> findByAvailableTrueAndCarIdNotNull();
}
