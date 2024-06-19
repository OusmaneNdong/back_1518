package com.fonctionpublique.services.logout;

import com.fonctionpublique.access.token.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogOutServiceImpl {

    private final VerificationTokenRepository verificationTokenRepository;

//    @Override
//    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        String authHeader = request.getHeader("Bearer ");
//        String token = null;
//        if(authHeader != null && authHeader.startsWith("Bearer ")){
//            return;
//        }
//    }
}
