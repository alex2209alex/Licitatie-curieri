package ro.fmi.unibuc.licitatie_curieri.common.exception;

public class ForbiddenException extends RuntimeException {
    private ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
