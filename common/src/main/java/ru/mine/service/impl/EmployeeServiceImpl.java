package ru.mine.service.impl;

import org.springframework.stereotype.Service;
import ru.mine.domain.Employee;
import ru.mine.repository.EmployeeRepository;
import ru.mine.service.EmployeeService;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Override
    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public Employee findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
