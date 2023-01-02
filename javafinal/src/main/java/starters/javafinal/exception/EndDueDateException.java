package starters.javafinal.exception;

public class EndDueDateException extends RuntimeException{
    public EndDueDateException() {
    }

    public EndDueDateException(String message) {
        super(message);
    }
}
