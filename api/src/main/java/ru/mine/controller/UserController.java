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
import ru.mine.dto.UserDTO;
import ru.mine.service.impl.UserServiceImpl;

import java.sql.Timestamp;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController implements
        RepresentationModelAssembler<User, EntityModel<User>> {

    private final UserServiceImpl repository;
    
    @Autowired
    public UserController(UserServiceImpl repository) {
        this.repository = repository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<User>> getAll() {
        return toCollectionModel(repository.findAll());
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<User> getSingle(@PathVariable Integer id) {
        return toModel(repository.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<User> create(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        user.setRole(SystemRoles.REGULAR_USER);
        return toModel(repository.save(user));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<User> update(@PathVariable Integer id,
                                    @RequestBody UserDTO userDTO) {
        User user = repository.findById(id);
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        return toModel(repository.save(user));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<User> changeRole(Integer id,
                                        SystemRoles role) {
        User user = repository.findById(id);
        user.setRole(role);
        return toModel(user);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<User> changeDeletedFlag(@PathVariable Integer id,
                                               boolean bool) {
        User user = repository.findById(id);
        user.setDeleted(bool);
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        return toModel(repository.save(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }


    /*Model Builder Section*/

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

