package io.tarrie.database.exceptions;

public class MalformedInputException extends RuntimeException {
        public MalformedInputException(String errorMessage, Throwable err) {
            super(errorMessage, err);
        }
        public MalformedInputException(String errorMessage) {
            super(errorMessage);
        }

}
