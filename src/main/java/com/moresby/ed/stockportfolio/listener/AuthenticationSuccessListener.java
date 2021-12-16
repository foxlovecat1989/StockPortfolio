package com.moresby.ed.stockportfolio.listener;

import com.moresby.ed.stockportfolio.domain.UserPrincipal;
import com.moresby.ed.stockportfolio.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener {

    private final LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        var principal = event.getAuthentication().getPrincipal();
        if(principal instanceof UserPrincipal) {
            UserPrincipal user = (UserPrincipal) principal;
            loginAttemptService.removeUserFromLoginAttemptCache(user.getUsername());
        }
    }
}
