package ru.mine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mine.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}

