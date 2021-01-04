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
import ru.mine.domain.SystemRoles;
import ru.mine.dto.EmployeeDTO;
import ru.mine.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/employees")
public class EmployeeController implements
        RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Employee>> getAll() {
        return toCollectionModel(repository.findAll());
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Employee> getSingle(@PathVariable Integer id) {
        return toModel(repository.findById(id).orElseThrow());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Employee> create(@RequestBody EmployeeDTO newEmployee) {
        Employee employee = new Employee();
        employee.setFullName(newEmployee.getFullName());
        employee.setBirthDate(newEmployee.getBirthDate());
        employee.setRegistration(newEmployee.getRegistration());
        employee.setHiringDate(LocalDate.now());
        employee.setPosition(SystemRoles.EMPLOYEE);
        //magic const to write typical first-time contract duration
        employee.setContractExpiration(LocalDate.now().plusYears(1));
        employee.setPayroll(newEmployee.getPayroll());
        employee.setPhoneNumber(newEmployee.getPhoneNumber());
        return toModel(repository.save(employee));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Employee> update(@PathVariable Integer id,
                                        @RequestBody EmployeeDTO newEmployee) {
        Employee employee = repository.findById(id).orElseThrow();
        employee.setFullName(newEmployee.getFullName());
        employee.setBirthDate(newEmployee.getBirthDate());
        employee.setRegistration(newEmployee.getRegistration());
        employee.setPayroll(newEmployee.getPayroll());
        employee.setPhoneNumber(newEmployee.getPhoneNumber());
        return toModel(repository.save(employee));
    }

    @PatchMapping("/contract")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Employee> extendContract(Integer employeeId,
                                                int years) {
        Employee employee = repository.findById(employeeId).orElseThrow();
        employee.setContractExpiration(employee.getContractExpiration().plusYears(years));
        return toModel(repository.save(employee));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Employee> changeFiredFlag(@PathVariable Integer id,
                                             boolean bool) {
        Employee employee = repository.findById(id).orElseThrow();
        employee.setFired(bool);
        return toModel(repository.save(employee));
    }

    @PatchMapping("/position")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Employee> changePosition(Integer id,
                                                SystemRoles role) {
        Employee employee = repository.findById(id).orElseThrow();
        employee.setPosition(role);
        return toModel(repository.save(employee));
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
