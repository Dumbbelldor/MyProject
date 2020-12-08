package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "m_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column
    private String login;

    @Column
    private String password;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column
    private String email;

    @Column
    private Timestamp created;

    @Column
    private Timestamp changed;
}
