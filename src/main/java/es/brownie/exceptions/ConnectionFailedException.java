package es.brownie.exceptions;

public class ConnectionFailedException extends RuntimeException {

    public ConnectionFailedException(Throwable cause) {
        super(cause);
    }
}
