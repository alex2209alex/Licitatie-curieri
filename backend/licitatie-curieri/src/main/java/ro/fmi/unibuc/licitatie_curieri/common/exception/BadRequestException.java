package ro.fmi.unibuc.licitatie_curieri.common.exception;

public class BadRequestException extends RuntimeException {
    private BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
