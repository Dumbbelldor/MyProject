package ru.mine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mine.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

}

