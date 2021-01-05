package ru.mine.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String login;

    private String password;

    private String email;
}
