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
import ru.mine.dto.DriverDTO;
import ru.mine.service.impl.DriverServiceImpl;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/drivers")
public class DriverController implements
        RepresentationModelAssembler<Driver, EntityModel<Driver>> {

    private final DriverServiceImpl repository;

    @Autowired
    public DriverController(DriverServiceImpl repository) {
        this.repository = repository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Driver>> getAll() {
        return toCollectionModel(repository.findAll());
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Driver> getSingle(@PathVariable Integer id) {
        return toModel(repository.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Driver> create(@RequestBody DriverDTO newDriver) {
        Driver driver = new Driver();
        driver.setEmployeeId(newDriver.getEmployeeId());
        driver.setLicenseId(newDriver.getLicenseId());
        driver.setLicenseExpDate(newDriver.getLicenseExpDate());
        return toModel(repository.save(driver));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Driver> update(@PathVariable Integer id,
                                      @RequestBody DriverDTO newDriver) {
        Driver driver = repository.findById(id);
        driver.setEmployeeId(newDriver.getEmployeeId());
        driver.setLicenseId(newDriver.getLicenseId());
        driver.setLicenseExpDate(newDriver.getLicenseExpDate());
        return toModel(repository.save(driver));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Driver> changeAvailableFlag(@PathVariable Integer id,
                                                  boolean bool) {
        Driver driver = repository.findById(id);
        driver.setAvailable(bool);
        return toModel(repository.save(driver));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Driver> assignCar(Integer driverId,
                                         Integer carId) {
        Driver driver = repository.findById(driverId);
        driver.setCarId(carId);
        return toModel(repository.save(driver));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    /*Model Builder Section*/

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
