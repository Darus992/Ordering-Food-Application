package pl.dariuszgilewicz.infrastructure.model.exception;

public class EntityAlreadyExistAuthenticationException extends RuntimeException {

    public EntityAlreadyExistAuthenticationException(final String msg) {
        super(msg);
    }
}
