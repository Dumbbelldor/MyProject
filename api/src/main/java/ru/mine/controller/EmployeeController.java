package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.Employee;
import ru.mine.repository.EmployeeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/employees")
public class EmployeeController implements
        RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    private final EmployeeRepository repository;

    private static final String MESSAGE = "Employee is not found by id: ";

    @Autowired
    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Employee>> getAll() {
        List<Employee> employees = repository.findAll();

        return toCollectionModel(employees);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Employee> getSingle(@PathVariable Integer id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return toModel(employee);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee flagAsFired(@PathVariable Integer id) {
        return repository.findById(id)
                .map(emp -> {
                    emp.setFired(true);
                    return repository.save(emp);
                })
                .orElseThrow( () -> new EntityNotFoundException(MESSAGE+id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }


    /*Model Builder Section*/
    @Override
    @NonNull
    public EntityModel<Employee> toModel(@NonNull Employee employee) {
        return EntityModel.of(employee,
                WebMvcLinkBuilder.linkTo(methodOn(EmployeeController.class).getSingle(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees"));
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<Employee>> toCollectionModel(Iterable<? extends Employee> employees) {
        return StreamSupport.stream(employees.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
