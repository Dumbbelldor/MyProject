package ru.mine.controller.assembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.mine.controller.MenuController;
import ru.mine.domain.Menu;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MenuModelAssembler implements
        RepresentationModelAssembler<Menu, EntityModel<Menu>> {

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
