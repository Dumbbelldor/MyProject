package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.Driver;
import ru.mine.repository.DriverRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/drivers")
public class DriverController implements
        RepresentationModelAssembler<Driver, EntityModel<Driver>> {

    private final DriverRepository repository;

    private static final String MESSAGE = "Driver is not found by id: ";

    @Autowired
    public DriverController(DriverRepository repository) {
        this.repository = repository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Driver>> getAll() {
        List<Driver> drivers = repository.findAll();

        return toCollectionModel(drivers);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Driver> getSingle(@PathVariable Integer id) {
        Driver driver = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return toModel(driver);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Driver create(@RequestBody Driver newDriver) {
        return repository.save(newDriver);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Driver flagAsBusy(@PathVariable Integer id) {
        return repository.findById(id)
                .map(drv -> {
                    drv.setAvailable(false);
                    return repository.save(drv);
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
    public EntityModel<Driver> toModel(@NonNull Driver driver) {
        return EntityModel.of(driver,
                WebMvcLinkBuilder.linkTo(methodOn(DriverController.class).getSingle(driver.getId())).withSelfRel(),
                linkTo(methodOn(DriverController.class).getAll()).withRel("drivers"));
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<Driver>> toCollectionModel(Iterable<? extends Driver> drivers) {
        return StreamSupport.stream(drivers.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
