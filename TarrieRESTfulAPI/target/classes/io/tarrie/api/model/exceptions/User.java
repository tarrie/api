package io.tarrie.api.model.exceptions;

public class User extends Exception{
    public User(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public User(String errorMessage) {
        super(errorMessage);
    }
}
