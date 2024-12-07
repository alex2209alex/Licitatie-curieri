package ro.fmi.unibuc.licitatie_curieri.dto;

public class VerificationRequestDto {
    private String username;
    private String code;

    // Getters și Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
