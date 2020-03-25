package io.tarrie.database.exceptions;

public class TarrieGroupException extends Exception {
    public TarrieGroupException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public TarrieGroupException(String errorMessage) {
        super(errorMessage);
    }

}

