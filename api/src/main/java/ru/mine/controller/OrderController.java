package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mine.controller.assembler.OrderModelAssembler;
import ru.mine.domain.Order;
import ru.mine.repository.OrderRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repository;

    private final OrderModelAssembler assembler;

    private static final String MESSAGE = "Order is not found by id: ";

    @Autowired
    public OrderController(OrderRepository repository, OrderModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Order>> getAll() {
        List<Order> orders = repository.findAll();

        return assembler.toCollectionModel(orders);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Order> getSingle(@PathVariable Integer id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return assembler.toModel(order);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestBody Order newOrder) {
        return repository.save(newOrder);
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
}
