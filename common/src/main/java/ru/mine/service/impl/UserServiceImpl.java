package ru.mine.service.impl;

import org.springframework.stereotype.Service;
import ru.mine.domain.User;
import ru.mine.repository.UserRepository;
import ru.mine.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
