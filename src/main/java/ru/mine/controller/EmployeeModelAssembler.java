package ru.mine.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.mine.domain.Employee;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    @Override
    @NonNull
    public EntityModel<Employee> toModel(@NonNull Employee employee) {
        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getSingle(employee.getId())).withSelfRel(),
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
