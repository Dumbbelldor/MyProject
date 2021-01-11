package ru.mine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.mine.domain.Product;
import ru.mine.repository.ProductRepository;
import ru.mine.service.ProductService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable("products")
    public List<Product> findAll() {
        List<Product> list = repository.findAll();
        log.info("Found {} products", list.size());
        return list;
    }

    @Override
    public Product save(Product product) {
        try {
            Product saved = repository.save(product);
            log.info("Product successfully has been saved");
            return saved;
        } catch (IllegalArgumentException e) {
            log.error("Entity must not be null");
            e.printStackTrace();
            return new Product();
        }
    }

    @Override
    @Cacheable("products")
    public Product findById(Integer id) {
        try {
            Product product = repository.findById(id).orElseThrow();
            log.info("Product successfully found by id: {}", id);
            return product;
        } catch (NoSuchElementException e) {
            log.error("Entity is not found by id: {}", id);
            e.printStackTrace();
            return new Product();
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
