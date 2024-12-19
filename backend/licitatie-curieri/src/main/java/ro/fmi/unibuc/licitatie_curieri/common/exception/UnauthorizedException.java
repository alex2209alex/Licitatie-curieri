package ro.fmi.unibuc.licitatie_curieri.common.exception;

public class UnauthorizedException extends RuntimeException {
    private UnauthorizedException() {}

    public UnauthorizedException(String message) {
        super(message);
    }
}
