package ru.mine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mine.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
