package ro.fmi.unibuc.licitatie_curieri.common.exception;

public class InternalServerErrorException extends RuntimeException {
    private InternalServerErrorException() {
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
