package io.tarrie.database.exceptions;

public class ProcessingException extends Exception {
        public ProcessingException(String errorMessage, Throwable err) {
            super(errorMessage, err);
        }
        public ProcessingException(String errorMessage) {
            super(errorMessage);
        }

}
