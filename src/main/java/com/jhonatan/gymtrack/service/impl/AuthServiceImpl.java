package com.jhonatan.gymtrack.service.impl;


import com.jhonatan.gymtrack.dto.authDto.AuthenticatedUserDTO;
import com.jhonatan.gymtrack.dto.authDto.LoginRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.RegisterRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.TokenDataDTO;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.exception.InvalidCredentialsException;
import com.jhonatan.gymtrack.exception.ResourceNotFoundException;
import com.jhonatan.gymtrack.mapper.UserMapper;
import com.jhonatan.gymtrack.repository.UserRepo;
import com.jhonatan.gymtrack.security.AuthToken;
import com.jhonatan.gymtrack.security.TokenUtil;
import com.jhonatan.gymtrack.service.IAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepo repo;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final TokenUtil tokenUtil;

    @Override
    @Transactional(readOnly = true)
    public AuthenticatedUserDTO login(LoginRequestDTO request, HttpServletResponse response) {

        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        User user = repo.findByEmail(request.getEmail());
        if (user == null || !encoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Email or Password Invalid");
        }

        Set<String> roles = Collections.emptySet();
        TokenDataDTO tokenData = new TokenDataDTO(user.getEmail(), roles);

        AuthToken authToken = tokenUtil.encodeToken(tokenData);

        //injeto o cookie na resposta
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", authToken.getToken())
                .httpOnly(true)
                .secure(false)      //mudar aqui pra true em prod (HTTPS)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return new AuthenticatedUserDTO(user.getId(), user.getEmail(), user.getName());

    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void register(RegisterRequestDTO request) {


        if(repo.existsByEmail(request.getEmail())){
            throw new InvalidCredentialsException("Email already exists");
        }
        User newUser = mapper.toUser(request);
        newUser.setPassword(encoder.encode(request.getPassword()));
        repo.save(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticatedUserDTO getCurrentUser(String email) {
        User user = repo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return new AuthenticatedUserDTO(user.getId(), user.getEmail(), user.getName());
    }
}
