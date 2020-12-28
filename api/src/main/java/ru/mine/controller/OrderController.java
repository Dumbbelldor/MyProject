package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.Menu;
import ru.mine.domain.Order;
import ru.mine.repository.DriverRepository;
import ru.mine.repository.OrderRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/orders")
public class OrderController implements
        RepresentationModelAssembler<Order, EntityModel<Order>> {

    private final OrderRepository repository;

    private final DriverRepository driverRepository;

    private static final String MESSAGE = "Order is not found by id: ";

    @Autowired
    public OrderController(OrderRepository repository,
                           DriverRepository driverRepository) {
        this.repository = repository;
        this.driverRepository = driverRepository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Order>> getAll() {
        List<Order> orders = repository.findAll();

        return toCollectionModel(orders);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Order> getSingle(@PathVariable Integer id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return toModel(order);
    }

    @GetMapping("requestCart")
    public List<Menu> show(){
        return new ArrayList<>(MenuController.cart.keySet());
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Order flagAsDelivered(@PathVariable Integer id) {
        return repository.findById(id)
                .map(order -> {
                    order.setDelivered(true);
                    return repository.save(order);
                })
                .orElseThrow( () -> new EntityNotFoundException(MESSAGE+id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    @Override
    @NonNull
    public EntityModel<Order> toModel(@NonNull Order order) {
        return EntityModel.of(order,
                WebMvcLinkBuilder.linkTo(methodOn(OrderController.class).getSingle(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAll()).withRel("orders"));
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<Order>> toCollectionModel(Iterable<? extends Order> orders) {
        return StreamSupport.stream(orders.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
