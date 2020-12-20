package ru.mine.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.mine.domain.Driver;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class DriverModelAssembler implements RepresentationModelAssembler<Driver, EntityModel<Driver>> {

    @Override
    @NonNull
    public EntityModel<Driver> toModel(@NonNull Driver driver) {
        return EntityModel.of(driver,
                linkTo(methodOn(DriverController.class).getSingle(driver.getId())).withSelfRel(),
                linkTo(methodOn(DriverController.class).getAll()).withRel("employees"));
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<Driver>> toCollectionModel(Iterable<? extends Driver> drivers) {
        return StreamSupport.stream(drivers.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
