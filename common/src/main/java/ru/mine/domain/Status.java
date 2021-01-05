package ru.mine.domain;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum Status {
    IN_PROGRESS, AWAITS, COMPLETED, DECLINED
}
