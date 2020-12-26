package ru.mine.domain;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum SystemRoles {

    REGULAR_USER,
    PREMIUM_USER,
    MODERATOR,
    ADMIN,

    EMPLOYEE,
    DIRECTOR,
    MANAGER,
    OPERATOR,
    TECHNICIAN,
    DRIVER

}
