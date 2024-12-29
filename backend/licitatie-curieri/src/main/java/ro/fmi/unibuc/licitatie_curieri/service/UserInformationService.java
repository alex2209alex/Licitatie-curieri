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

        return userRepository.findById(userId).orElseThrow(() -> new ForbiddenException(ErrorMessageUtils.AUTHENTICATION_IS_NULL));
    }

    public boolean isCurrentUserClient() {
        return UserType.CLIENT == getCurrentUser().getUserType();
    }

    public boolean isCurrentUserCourier() {
        return UserType.COURIER == getCurrentUser().getUserType();
    }

    public boolean isCurrentUserRestaurantAdmin() {
        return UserType.RESTAURANT_ADMIN == getCurrentUser().getUserType();
    }

    private Long getCurrentUserId() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return Long.parseLong(authentication.getName());
        }

        throw new ForbiddenException(ErrorMessageUtils.AUTHENTICATION_IS_NULL);
    }
}
