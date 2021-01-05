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
import ru.mine.service.impl.ProductServiceImpl;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/products")
public class ProductController implements
        RepresentationModelAssembler<Product, EntityModel<Product>> {

    private final ProductServiceImpl repository;

    @Autowired
    public ProductController(ProductServiceImpl repository) {
        this.repository = repository;
    }

    /*General Section*/

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Product>> getAll() {
        return toCollectionModel(repository.findAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Product> getSingle(@PathVariable Integer id) {
        return toModel(repository.findById(id));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Product> create(@RequestBody ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setAvailable(true);
        return toModel(repository.save(product));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Product> update(Integer id,
                                       @RequestBody ProductDTO productDTO) {
        Product product = repository.findById(id);

        if (productDTO.getName() != null) product.setName(productDTO.getName());
        if (productDTO.getPrice() != 0) product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        return toModel(repository.save(product));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Product> changeAvailableFlag(@PathVariable Integer id, boolean bool) {
        Product product = repository.findById(id);
        product.setAvailable(bool);
        return toModel(repository.save(product));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
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
