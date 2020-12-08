package ru.mine.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Integer id) {
        super("User by id: "+id+" is not found.");
    }
}
