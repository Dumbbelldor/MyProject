package ru.mine.service;

import ru.mine.domain.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User save(User user);

    User findById(Integer id);

    void deleteById(Integer id);
}
