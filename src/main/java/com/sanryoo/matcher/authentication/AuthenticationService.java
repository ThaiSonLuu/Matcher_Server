package com.sanryoo.matcher.authentication;

import com.sanryoo.matcher.modal.AuthUser;
import com.sanryoo.matcher.modal.Role;
import com.sanryoo.matcher.modal.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;

    public String generateNewToken(User user) {
        return jwtService.generateToken(new AuthUser(user.getUsername(), user.getPassword(), Role.USER));
    }

}