package ru.mine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mine.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
