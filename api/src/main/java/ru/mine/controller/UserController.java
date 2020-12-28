package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.SystemRoles;
import ru.mine.domain.User;
import ru.mine.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController implements RepresentationModelAssembler<User, EntityModel<User>>{

    private final UserRepository repository;
    
    private static final String MESSAGE = "User is not found by id: ";

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<User>> getAll() {
        List<User> users = repository.findAll();

        return toCollectionModel(users);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<User> getSingle(@PathVariable Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return toModel(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(String login, String password, String email) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        user.setRole(SystemRoles.REGULAR_USER);
        return repository.save(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User update(@PathVariable Integer id,
                       @RequestBody User newUser) {
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

    @Override
    @NonNull
    public EntityModel<User> toModel(@NonNull User user) {
        return EntityModel.of(user,
                WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getSingle(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("users"));
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> users) {
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}

