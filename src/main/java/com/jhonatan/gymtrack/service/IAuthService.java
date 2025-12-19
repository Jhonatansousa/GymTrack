package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.authDto.LoginRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.RegisterRequestDTO;
import com.jhonatan.gymtrack.security.AuthToken;

public interface IAuthService {
    AuthToken login (LoginRequestDTO loginRequestDTO);

    void register(RegisterRequestDTO registerRequestDTO);
}
