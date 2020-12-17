package ru.mine.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.mine.domain.User;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    @NonNull
    public EntityModel<User> toModel(@NonNull User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getSingle(user.getId())).withSelfRel(),
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
