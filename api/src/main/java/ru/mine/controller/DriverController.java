package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mine.controller.assembler.DriverModelAssembler;
import ru.mine.domain.Driver;
import ru.mine.repository.DriverRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverRepository repository;

    private final DriverModelAssembler assembler;

    private static final String MESSAGE = "Driver is not found by id: ";

    @Autowired
    public DriverController(DriverRepository repository, DriverModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Driver>> getAll() {
        List<Driver> drivers = repository.findAll();

        return assembler.toCollectionModel(drivers);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Driver> getSingle(@PathVariable Integer id) {
        Driver driver = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return assembler.toModel(driver);
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
}
