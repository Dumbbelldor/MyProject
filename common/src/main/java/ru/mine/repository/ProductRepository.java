package ru.mine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mine.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
