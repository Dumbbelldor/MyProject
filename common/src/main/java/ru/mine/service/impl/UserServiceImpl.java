package ru.mine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mine.domain.User;
import ru.mine.repository.UserRepository;
import ru.mine.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll() {
        List<User> list = repository.findAll();
        log.info("Found {} users", list.size());
        return list;
    }

    @Override
    public User save(User user) {
        try {
            User saved = repository.save(user);
            log.info("User successfully has been saved");
            return saved;
        } catch (IllegalArgumentException e) {
            log.error("Entity must not be null");
            e.printStackTrace();
            return new User();
        }
    }

    @Override
    public User findById(Integer id) {
        try {
            User user = repository.findById(id).orElseThrow();
            log.info("User successfully found by id: {}", id);
            return user;
        } catch (NoSuchElementException e) {
            log.error("Entity is not found by id: {}", id);
            e.printStackTrace();
            return new User();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            repository.deleteById(id);
            log.info("Successfully deleted by id: {}", id);
        } catch (IllegalArgumentException e) {
            log.error("Such element cannot be found to perform removal");
            e.printStackTrace();
        }
    }
}
