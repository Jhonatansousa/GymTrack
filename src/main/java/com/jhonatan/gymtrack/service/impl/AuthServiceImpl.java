package com.jhonatan.gymtrack.service.impl;


import com.jhonatan.gymtrack.dto.authDto.LoginRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.RegisterRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.TokenDataDTO;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.exception.InvalidCredentialsException;
import com.jhonatan.gymtrack.mapper.UserMapper;
import com.jhonatan.gymtrack.repository.UserRepo;
import com.jhonatan.gymtrack.security.AuthToken;
import com.jhonatan.gymtrack.security.TokenUtil;
import com.jhonatan.gymtrack.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepo repo;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    @Transactional(readOnly = true)
    public AuthToken login(LoginRequestDTO request) {

        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        User res = repo.findByEmail(request.getEmail());
        if (res == null || !encoder.matches(request.getPassword(), res.getPassword())) {
            throw new InvalidCredentialsException("Email or Password Invalid");
        }

        Set<String> roles = Collections.emptySet();

        TokenDataDTO tokenData = new TokenDataDTO(res.getEmail(), roles);

        return TokenUtil.encodeToken(tokenData);

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
}
