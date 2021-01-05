package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mine.domain.Product;
import ru.mine.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final ProductServiceImpl repository;

    private final HttpSession session;

    private final Map<Product, Integer> cart = new LinkedHashMap<>();

    @Autowired
    public CartController(ProductServiceImpl repository, HttpSession session) {
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
        Product product = repository.findById(id);
        if (quantity > 0 && quantity < product.getQuantity()) {
            cart.put(product, quantity);
            session.setAttribute("cart", cart);
            return product.getName()+" x"+quantity+" have been added to your cart";
        } else return "Insufficient quantity of that product";
    }

    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public String update(Integer id, Integer newQuantity) {
        Map<Product, Integer> cartMap = getCartMap();
        Product product = repository.findById(id);

        if (newQuantity == 0) {
            deleteItem(id);
            return "All items "+product.getName()+" have been deleted";
        }

        if (newQuantity > 0 && newQuantity < product.getQuantity()) {

            if (cartMap.containsKey(product)) {
                cartMap.replace(product, newQuantity);
                session.setAttribute("cart", cartMap);
                return "Quantity of "+product.getName()+" has been changed to "+newQuantity;
            } else {
                cartMap.put(product, newQuantity);
                session.setAttribute("cart", cartMap);
                return product.getName()+" x"+newQuantity+" have been added to your cart";
            }
        } else return "Operation declined: not enough items";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteItem(@PathVariable Integer id) {
        Map<Product, Integer> cartMap = getCartMap();
        Product product = repository.findById(id);

        if (cartMap.containsKey(product)) {
            cartMap.remove(product);
            session.setAttribute("cart", cart);
            return product.getName()+" has been removed from cart";
        } else {
            return "You've got no such item in your cart";
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String clearCart() {
        Map<Product, Integer> cartMap = getCartMap();
        cartMap.clear();
        session.setAttribute("cart", cartMap);
        return "Your cart has been cleansed successfully";
    }

    @SuppressWarnings("unchecked")
    private Map<Product, Integer> getCartMap() {
        if (session.getAttribute("cart") != null) {
            return (Map<Product, Integer>) session.getAttribute("cart");
        } else throw new NullPointerException("Cart is not yet filled with stuff");
    }
}
