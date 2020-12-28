package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "products")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "available")
    private boolean available;

    @Column
    private Integer price;

}

