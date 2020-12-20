package ru.mine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mine.domain.Driver;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
}
