package ru.mine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mine.controller.assembler.MenuModelAssembler;
import ru.mine.domain.Menu;
import ru.mine.repository.MenuRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuRepository repository;

    private final MenuModelAssembler assembler;

    private static final String MESSAGE = "Menu item is not found by id: ";

    @Autowired
    public MenuController(MenuRepository repository, MenuModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /*All items*/
    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public CollectionModel<EntityModel<Menu>> getAll() {
        List<Menu> menu = repository.findAll();

        return assembler.toCollectionModel(menu);
    }

    /*Single item*/
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public EntityModel<Menu> getSingle(@PathVariable Integer id) {
        Menu menu = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE+id));

        return assembler.toModel(menu);
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
}
