package io.tarrie.database.exceptions;

public class TarrieExistenceError extends Exception {
    public TarrieExistenceError(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public TarrieExistenceError(String errorMessage) {
        super(errorMessage);
    }

}
