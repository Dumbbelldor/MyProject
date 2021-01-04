package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    static final long serialVersionUID = 123L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String login;

    @Column
    private String password;

    @Column(name = "deleted")
    private boolean deleted;

    @Column
    private String email;

    @Column
    private Timestamp created;

    @Column
    private Timestamp changed;

    @Column
    @Enumerated(EnumType.STRING)
    private SystemRoles role;
}
