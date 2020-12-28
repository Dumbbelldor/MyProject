package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.Car;
import ru.mine.repository.CarRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cars")
public class CarController implements
        RepresentationModelAssembler<Car, EntityModel<Car>> {

    private final CarRepository repository;

    private static final String MESSAGE = "Car is not found by id: ";

    @Autowired
    public CarController(CarRepository repository) {
        this.repository = repository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Car>> getAll() {
        List<Car> cars = repository.findAll();

        return toCollectionModel(cars);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Car> getSingle(@PathVariable Integer id) {
        Car car = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return toModel(car);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Car create(@RequestBody Car newCar) {
        return repository.save(newCar);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Car flagAsUnavailable(@PathVariable Integer id) {
        return repository.findById(id)
                .map(car -> {
                    car.setAvailable(false);
                    return repository.save(car);
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
