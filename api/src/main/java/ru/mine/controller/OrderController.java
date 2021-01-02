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
import ru.mine.domain.Order;
import ru.mine.domain.Status;
import ru.mine.repository.OrderRepository;
import ru.mine.utils.DriverAssignation;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/orders")
@SessionAttributes(value = "cart")
public class OrderController implements
        RepresentationModelAssembler<Order, EntityModel<Order>> {

    private final OrderRepository repository;

    private final HttpSession session;

    @Autowired
    public OrderController(OrderRepository repository, HttpSession session) {
        this.repository = repository;
        this.session = session;
    }

    /*General Section*/

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Order>> getAll() {
        List<Order> orders = repository.findAll();
        return toCollectionModel(orders);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Order> getSingle(@PathVariable Integer id) {
        Order order = validFindById(id);
        return toModel(order);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Order> changeStatus(@PathVariable Integer id, Status status) {
        Order order = validFindById(id);
        order.setStatus(status);
        return toModel(repository.save(order));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    /*Cart Section*/

    @GetMapping("/getCart")
    public Map<Product, Integer> showCart() {
        return getCartMap();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Order> completeOrder(Integer customerId) {
        Order order = new Order();
        Map<Product, Integer> cart = getCartMap();
        List<Product> products = new LinkedList<>(cart.keySet());
        List<Integer> quantity = new LinkedList<>(cart.values());

        /*This section simply creating order line and counts its full price*/
        String orderLine = "";
        int totalPrice = 0;
        if (products.size() == quantity.size()) {
            for (int i = 0; i < products.size(); i++) {
                orderLine = orderLine.concat("\"" + products.get(i).getName()
                        + " x" + quantity.get(i) + "\"\n");
                totalPrice += products.get(i).getPrice() * quantity.get(i);
            }
        } else throw new ArithmeticException("That shouldn't have happened");

        /*This section searches for available driver in database,
        puts them into a list, randomly chooses one of them in [1, size) range,
        assign him to the order, then flags him as busy (unavailable)*/
        Integer driverId;
        if (DriverAssignation.isAnyoneReady()) {
            driverId = DriverAssignation.assignAndGetId(true);
            order.setStatus(Status.IN_PROGRESS);
        } else {
            driverId = DriverAssignation.assignAndGetId(false);
            order.setStatus(Status.AWAITS);
        }

        order.setCustomerId(customerId);
        order.setOrderLine(orderLine);
        order.setOrderTime(new Timestamp(System.currentTimeMillis()));
        order.setCourierId(driverId);
        order.setTotalPrice(totalPrice);

        return toModel(repository.save(order));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public String update() {
        return "Your order is already in processing, to change anything from now you need to contact our manager";
    }

    /*Misc*/

    @SuppressWarnings("unchecked")
    private Map<Product, Integer> getCartMap() {
        if (session.getAttribute("cart") != null) {
            return (Map<Product, Integer>) session.getAttribute("cart");
        } else throw new NullPointerException("Cart is not yet filled with stuff");
    }

    private Order validFindById(Integer id) {
        if (id > 0) {
            return repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Order is not found by id: " + id));
        } else throw new IllegalArgumentException("Id must be positive and non-null");
    }

    /*Model Builder Section*/

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
