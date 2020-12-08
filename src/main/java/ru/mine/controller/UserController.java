package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.User;
import ru.mine.exception.UserNotFoundException;
import ru.mine.repository.UserRepository;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @GetMapping("/{id}")
    public User getSingleUser(@PathVariable Integer id) {
        return userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(id));
    }

    @PutMapping("/{id}")
    public User modifyUser(@RequestBody User newUser, @PathVariable Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setLogin(newUser.getLogin());
                    user.setPassword(newUser.getPassword());
                    user.setEmail(newUser.getEmail());
                    user.setChanged(new Timestamp(System.currentTimeMillis()));
                    user.setRole(newUser.getRole());
                    return userRepository.save(user);
                })
                .orElseThrow( () -> new UserNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void dropUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

}
