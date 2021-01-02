package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.Product;
import ru.mine.dto.ProductDTO;
import ru.mine.repository.ProductRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/products")
public class ProductController implements
        RepresentationModelAssembler<Product, EntityModel<Product>> {

    private final ProductRepository repository;

    @Autowired
    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    /*General Section*/

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Product>> getAll() {
        List<Product> products = repository.findAll();
        return toCollectionModel(products);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Product> getSingle(@PathVariable Integer id) {
        Product product = findById(id);
        return toModel(product);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Product> create(@RequestBody ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setAvailable(true);
        return toModel(repository.save(product));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Product> update(Integer id, ProductDTO productDTO) {
        Product product = findById(id);

        if (productDTO.getName() != null) product.setName(productDTO.getName());
        if (productDTO.getPrice() != 0) product.setPrice(productDTO.getPrice());
        return toModel(repository.save(product));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Product> flagAsUnavailable(@PathVariable Integer id) {
        Product product = findById(id);
        product.setAvailable(false);
        return toModel(repository.save(product));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    /*Misc*/

    private Product findById(Integer id) {
        if (id > 0) {
            return repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Product is not found by id: " + id));
        } else throw new IllegalArgumentException("Id must be positive and non-null");
    }

    /*Model Builder Section*/

    @Override
    @NonNull
    public EntityModel<Product> toModel(@NonNull Product product) {
        return EntityModel.of(product,
                WebMvcLinkBuilder.linkTo(methodOn(ProductController.class).getSingle(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAll()).withRel("product"));
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<Product>> toCollectionModel(Iterable<? extends Product> products) {
        return StreamSupport.stream(products.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
