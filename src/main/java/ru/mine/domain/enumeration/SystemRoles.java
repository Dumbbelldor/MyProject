package ru.mine.domain.enumeration;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum SystemRoles {

    REGULAR_USER,
    PREMIUM_USER,
    MODERATOR,
    ADMIN;
}
