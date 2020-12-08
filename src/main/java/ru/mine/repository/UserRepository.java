package ru.mine.repository;

import org.springframework.data.repository.CrudRepository;
import ru.mine.domain.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
