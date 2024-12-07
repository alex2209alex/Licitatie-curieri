package ro.fmi.unibuc.licitatie_curieri.common.exception;

public class NotFoundException extends RuntimeException {
    private NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
