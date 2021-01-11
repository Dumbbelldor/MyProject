package ru.mine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.mine.domain.Employee;
import ru.mine.repository.EmployeeRepository;
import ru.mine.service.EmployeeService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable("employees")
    public List<Employee> findAll() {
        List<Employee> list = repository.findAll();
        log.info("Found {} employees", list.size());
        return list;
    }

    @Override
    public Employee save(Employee employee) {
        try {
            Employee saved = repository.save(employee);
            log.info("Employee successfully has been saved");
            return saved;
        } catch (IllegalArgumentException e) {
            log.error("Entity must not be null");
            e.printStackTrace();
            return new Employee();
        }
    }

    @Override
    @Cacheable("employees")
    public Employee findById(Integer id) {
        try {
            Employee employee = repository.findById(id).orElseThrow();
            log.info("Employee successfully found by id: {}", id);
            return employee;
        } catch (NoSuchElementException e) {
            log.error("Entity is not found by id: {}", id);
            e.printStackTrace();
            return new Employee();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            repository.deleteById(id);
            log.info("Successfully deleted by id: {}", id);
        } catch (IllegalArgumentException e) {
            log.error("Such element cannot be found to perform removal");
            e.printStackTrace();
        }
    }
}
