package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.User;
import ru.mine.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;
    
    private final UserModelAssembler assembler;

    private static final String MESSAGE = "User is not found by id: ";

    @Autowired
    public UserController(UserRepository repository, UserModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<User>> getAll() {
        List<User> users = repository.findAll();

        return assembler.toCollectionModel(users);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<User> getSingle(@PathVariable Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return assembler.toModel(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User newUser) {
        return repository.save(newUser);
    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public User smartCreateUser(String login, String password, String email) {
//        User user = new User();
//        user.setLogin(login);
//        user.setPassword(password);
//        user.setEmail(email);
//        user.setCreated(new Timestamp(System.currentTimeMillis()));
//        user.setChanged(new Timestamp(System.currentTimeMillis()));
//        user.setRole(SystemRoles.REGULAR_USER);
//        return repository.save(user);
//    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User update(@RequestBody User newUser, @PathVariable Integer id) {
        return repository.findById(id)
                .map(user -> {
                    user.setLogin(newUser.getLogin());
                    user.setPassword(newUser.getPassword());
                    user.setEmail(newUser.getEmail());
                    user.setChanged(new Timestamp(System.currentTimeMillis()));
                    user.setRole(newUser.getRole());
                    return repository.save(user);
                })
                .orElseThrow( () -> new EntityNotFoundException(MESSAGE+id));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User flagAsDeleted(@PathVariable Integer id) {
        return repository.findById(id)
                .map(user -> {
                    user.setDeleted(true);
                    user.setChanged(new Timestamp(System.currentTimeMillis()));
                    return repository.save(user);
                })
                .orElseThrow( () -> new EntityNotFoundException(MESSAGE+id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }

}
