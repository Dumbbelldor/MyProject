package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.Product;
import ru.mine.repository.ProductRepository;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository repository;

    private final HttpSession session;

    private final Map<Product, Integer> cart = new LinkedHashMap<>();

    @Autowired
    public CartController(ProductRepository repository, HttpSession session) {
        this.repository = repository;
        this.session = session;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public Map<Product, Integer> show() {
        return getCartMap();
    }

    @GetMapping("/form")
    @ResponseStatus(HttpStatus.OK)
    public String addItem(Integer id, Integer quantity) {
        if (id > 0 && quantity > 0) {
            Product product = findById(id);
            cart.put(product, quantity);
            session.setAttribute("cart", cart);
            return product.getName()+" x"+quantity+" have been added to your cart";
        } else return "Quantity/id must be positive and non-null";
    }

    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public String update(Integer id, Integer newQuantity) {
        Map<Product, Integer> cartMap = getCartMap();
        Product product = findById(id);

        if (newQuantity > 0) {
            if (cartMap.containsKey(product)) {
                cartMap.replace(product, newQuantity);
                session.setAttribute("cart", cartMap);
                return "Quantity of "+product.getName()+" has been changed to "+newQuantity;
            } else {
                cartMap.put(product, newQuantity);
                session.setAttribute("cart", cartMap);
                return product.getName()+" x"+newQuantity+" have been added to your cart";
            }
        }

        if (newQuantity == 0) {
            delete(id);
            return "Success";
        } else return "Quantity cannot be negative";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete(@PathVariable Integer id) {
        Map<Product, Integer> cartMap = getCartMap();
        Product product = findById(id);

        if (cartMap.containsKey(product)) {
            cartMap.remove(product);
            session.setAttribute("cart", cart);
            return product.getName()+" has been removed from cart";
        } else {
            return "You've got no such item in your cart";
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Product, Integer> getCartMap() {
        if (session.getAttribute("cart") != null) {
            return (Map<Product, Integer>) session.getAttribute("cart");
        } else throw new NullPointerException("Cart is not yet filled with stuff");
    }

    private Product findById(Integer id) {
        if (id > 0) {
            return repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Product is not found by id: " + id));
        } else throw new IllegalArgumentException("Id must be positive and non-null");
    }
}
