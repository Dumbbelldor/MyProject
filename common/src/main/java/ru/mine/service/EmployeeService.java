package ru.mine.service;

import ru.mine.domain.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();

    Employee save(Employee employee);

    Employee findById(Integer id);

    void deleteById(Integer id);
}
