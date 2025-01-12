package ro.fmi.unibuc.licitatie_curieri.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.fmi.unibuc.licitatie_curieri.common.exception.ForbiddenException;
import ro.fmi.unibuc.licitatie_curieri.common.utils.ErrorMessageUtils;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.User;
import ro.fmi.unibuc.licitatie_curieri.domain.user.entity.UserType;
import ro.fmi.unibuc.licitatie_curieri.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserInformationService {
    private final UserRepository userRepository;

    public void ensureCurrentUserIsVerified() {
        if (!getCurrentUser().isVerified()) {
            throw new ForbiddenException(ErrorMessageUtils.USER_IS_UNVERIFIED);
        }
    }

    public User getCurrentUser() {
        val userId = getCurrentUserId();

        return userRepository.findById(userId).orElseThrow(() -> new ForbiddenException(ErrorMessageUtils.AUTHENTICATION_TOKEN_IS_INVALID));
    }

    public boolean isCurrentUserClient() {
        return UserType.CLIENT.name().equals(getCurrentUserType());
    }

    public boolean isCurrentUserCourier() {
        return UserType.COURIER.name().equals(getCurrentUserType());
    }

    public boolean isCurrentUserRestaurantAdmin() {
        return UserType.RESTAURANT_ADMIN.name().equals(getCurrentUserType());
    }

    private Long getCurrentUserId() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return Long.parseLong(authentication.getName());
        }

        throw new ForbiddenException(ErrorMessageUtils.AUTHENTICATION_TOKEN_IS_INVALID);
    }

    private String getCurrentUserType() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (String) authentication.getCredentials();
        }

        throw new ForbiddenException(ErrorMessageUtils.AUTHENTICATION_TOKEN_IS_INVALID);
    }
}
