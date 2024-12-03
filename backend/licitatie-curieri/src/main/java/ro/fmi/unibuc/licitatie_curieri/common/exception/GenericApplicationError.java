package ro.fmi.unibuc.licitatie_curieri.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GenericApplicationError {
    private final int status;
    private final String detail;
}
