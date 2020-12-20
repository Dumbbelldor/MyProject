package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mine.controller.assembler.EmployeeModelAssembler;
import ru.mine.domain.Employee;
import ru.mine.repository.EmployeeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    private final EmployeeModelAssembler assembler;

    private static final String MESSAGE = "Employee is not found by id: ";

    @Autowired
    public EmployeeController(EmployeeRepository repository,
                              EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Employee>> getAll() {
        List<Employee> employees = repository.findAll();

        return assembler.toCollectionModel(employees);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Employee> getSingle(@PathVariable Integer id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return assembler.toModel(employee);
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
}
