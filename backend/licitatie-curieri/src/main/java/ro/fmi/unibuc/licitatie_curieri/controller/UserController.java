package ro.fmi.unibuc.licitatie_curieri.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.fmi.unibuc.licitatie_curieri.common.utils.LogMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.controller.user.api.UserApi;
import ro.fmi.unibuc.licitatie_curieri.controller.user.models.*;
import ro.fmi.unibuc.licitatie_curieri.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public UserCreationResponseDto createUser(
            @RequestBody UserCreationDto userCreationDto
    ) {
        log.info(String.format(LogMessageUtils.CREATE_USER, userCreationDto.getEmail(), userCreationDto.getUserType()));

        return userService.createUser(userCreationDto);
    }

    @Override
    public TokenResponseDto verifyUser(
            @PathVariable("email") String email,
            @RequestBody UserVerificationDto userVerificationDto
    ) {
        log.info(String.format(LogMessageUtils.VERIFY_USER, email));

        return userService.verifyUser(email, userVerificationDto);
    }

    @Override
    public void authenticateUser(
            @RequestBody UserLoginDto userLoginDto
    ){
        log.info(String.format(LogMessageUtils.AUTHENTICATE_USER, userLoginDto.getEmail(), userLoginDto.getPassword()));

        userService.loginUser(userLoginDto);
    }

    @Override
    public TokenResponseDto getTwoFACodeUser(@RequestBody UserTwoFAVerificationDto userTwoFAVerificationDto) {
        log.info(String.format(LogMessageUtils.TWO_FACTOR_AUTH_USER,
                userTwoFAVerificationDto.getEmail(),
                userTwoFAVerificationDto.getVerificationCode()
        ));

        return userService.getTwoFACodeUser(userTwoFAVerificationDto);
    }
}
