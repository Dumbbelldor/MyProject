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
import ru.mine.dto.CarDTO;
import ru.mine.repository.CarRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cars")
public class CarController implements
        RepresentationModelAssembler<Car, EntityModel<Car>> {

    private final CarRepository repository;

    @Autowired
    public CarController(CarRepository repository) {
        this.repository = repository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Car>> getAll() {
        return toCollectionModel(repository.findAll());
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Car> getSingle(@PathVariable Integer id) {
        return toModel(repository.findById(id).orElseThrow());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Car> create(@RequestBody CarDTO newCar) {
        Car car = new Car();
        car.setModel(newCar.getModel());
        car.setPlateNumber(newCar.getPlateNumber());
        car.setVin(newCar.getVin());
        car.setColor(newCar.getColor());
        return toModel(repository.save(car));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Car> update(@PathVariable Integer id,
                                   @RequestBody CarDTO newCar) {
        Car car = repository.findById(id).orElseThrow();
        car.setModel(newCar.getModel());
        car.setPlateNumber(newCar.getPlateNumber());
        car.setVin(newCar.getVin());
        car.setColor(newCar.getColor());
        return toModel(repository.save(car));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Car> assignDriver(Integer id,
                                         Integer driverId) {
        Car car = repository.findById(id).orElseThrow();
        car.setDriverId(driverId);
        return toModel(repository.save(car));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Car> changeAvailableFlag(@PathVariable Integer id,
                                               boolean bool) {
        Car car = repository.findById(id).orElseThrow();
        car.setAvailable(bool);
        return toModel(repository.save(car));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Car> changeDeletedStatus(Integer id,
                                               boolean bool) {
        Car car = repository.findById(id).orElseThrow();
        car.setDeleted(bool);
        return toModel(repository.save(car));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }


    /*Model Builder Section*/
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
