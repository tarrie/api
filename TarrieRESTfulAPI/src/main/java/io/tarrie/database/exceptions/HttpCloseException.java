package io.tarrie.database.exceptions;


public class HttpCloseException extends Exception {
        public HttpCloseException(String errorMessage, Throwable err) {
            super(errorMessage, err);
        }
        public HttpCloseException(String errorMessage) {
            super(errorMessage);
        }

}
