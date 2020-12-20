package ru.mine.controller.assembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.mine.controller.CarController;
import ru.mine.domain.Car;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarModelAssembler implements
        RepresentationModelAssembler<Car, EntityModel<Car>> {

    @Override
    @NonNull
    public EntityModel<Car> toModel(@NonNull Car car) {
        return EntityModel.of(car,
                WebMvcLinkBuilder.linkTo(methodOn(CarController.class).getSingle(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).getAll()).withRel("cars"));
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<Car>> toCollectionModel(Iterable<? extends Car> cars) {
        return StreamSupport.stream(cars.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
