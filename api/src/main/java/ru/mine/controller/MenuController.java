package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mine.domain.Menu;
import ru.mine.repository.MenuRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/menu")
//@SessionAttributes("menuMap")
public class MenuController implements
        RepresentationModelAssembler<Menu, EntityModel<Menu>> {

    private final MenuRepository repository;

    public static final Map<Menu, Integer> cart = new LinkedHashMap<>();

    private static final String MESSAGE = "Menu item is not found by id: ";

    @Autowired
    public MenuController(MenuRepository repository) {
        this.repository = repository;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Menu>> getAll() {
        List<Menu> menu = repository.findAll();

        return toCollectionModel(menu);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Menu> getSingle(@PathVariable Integer id) {
        Menu menu = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return toModel(menu);
    }

    @GetMapping("/form")
    public void addItemToCart(Integer id, Integer quantity) {
        Optional<Menu> menuOptional = repository.findById(id);
        Menu menu = null;
        if (menuOptional.isPresent()) {
            menu = menuOptional.get();
        }
        cart.put(menu, quantity);
    }

    @GetMapping("/show")
    public CollectionModel<EntityModel<Menu>> showCart() {
        List<Menu> list = new ArrayList<>(cart.keySet());
        return toCollectionModel(list);
    }

//    @GetMapping("/form")
//    public String showForm(
//            Model model,
//            @ModelAttribute("menuMap") Map<Menu, Integer> map) {
//        model.addAttribute("menuItem", new Menu());
//        return "sessionAttribute";
//    }

    @PostMapping("/form")
    public void create(
            @ModelAttribute Menu menu,
            @ModelAttribute("menuMap") Map<Menu, Integer> map,
            RedirectAttributes attributes) {
        menu.setName("Тестовые блюда");
        menu.setPrice(200);
        map.put(menu,3);
        attributes.addFlashAttribute("menuMap", map);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Menu create(@RequestBody Menu newMenu) {
        return repository.save(newMenu);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Menu flagAsUnavailable(@PathVariable Integer id) {
        return repository.findById(id)
                .map(menu -> {
                    menu.setAvailable(true);
                    return repository.save(menu);
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
    public EntityModel<Menu> toModel(@NonNull Menu menu) {
        return EntityModel.of(menu,
                WebMvcLinkBuilder.linkTo(methodOn(MenuController.class).getSingle(menu.getId())).withSelfRel(),
                linkTo(methodOn(MenuController.class).getAll()).withRel("menu"));
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<Menu>> toCollectionModel(Iterable<? extends Menu> menu) {
        return StreamSupport.stream(menu.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
