package org.rutebanken.proxynator;

/**
 *
 */
public class ProxynatorException extends RuntimeException {

    public ProxynatorException(String message) {
        super(message);
    }

    public ProxynatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
