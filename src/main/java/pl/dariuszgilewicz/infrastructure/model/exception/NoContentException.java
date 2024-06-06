package pl.dariuszgilewicz.infrastructure.model.exception;

public class NoContentException extends RuntimeException {

    public NoContentException(String msg) {
        super(msg);
    }
}
