package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mine.controller.assembler.CarModelAssembler;
import ru.mine.domain.Car;
import ru.mine.repository.CarRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarRepository repository;

    private final CarModelAssembler assembler;

    private static final String MESSAGE = "Car is not found by id: ";

    @Autowired
    public CarController(CarRepository repository, CarModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Car>> getAll() {
        List<Car> cars = repository.findAll();

        return assembler.toCollectionModel(cars);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Car> getSingle(@PathVariable Integer id) {
        Car car = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return assembler.toModel(car);
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
}
