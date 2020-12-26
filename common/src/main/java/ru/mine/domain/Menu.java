package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "m_menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column
    private Integer price;

}

