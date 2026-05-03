package com.jhonatan.gymtrack.facade;

import com.jhonatan.gymtrack.dto.authDto.AuthenticatedUserDTO;
import com.jhonatan.gymtrack.dto.authDto.LoginRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.RegisterRequestDTO;
import com.jhonatan.gymtrack.security.AuthToken;
import com.jhonatan.gymtrack.service.IAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {
    private final IAuthService authService;

    public AuthenticatedUserDTO registerAndLogin(RegisterRequestDTO request, HttpServletResponse response) {
        authService.register(request);
        System.out.println("Registrado!!");
        System.out.println(request.getPassword());
        return authService.login(new LoginRequestDTO(request.getEmail(), request.getPassword()), response);
    }
}
