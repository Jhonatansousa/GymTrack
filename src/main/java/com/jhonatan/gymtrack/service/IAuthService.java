package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.authDto.AuthenticatedUserDTO;
import com.jhonatan.gymtrack.dto.authDto.LoginRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.RegisterRequestDTO;
import com.jhonatan.gymtrack.security.AuthToken;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {
    AuthenticatedUserDTO login (LoginRequestDTO request, HttpServletResponse response);

    void register(RegisterRequestDTO registerRequestDTO);

    AuthenticatedUserDTO getCurrentUser(String email);
}
