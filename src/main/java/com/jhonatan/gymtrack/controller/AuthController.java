package com.jhonatan.gymtrack.controller;


import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.authDto.LoginRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.RegisterRequestDTO;
import com.jhonatan.gymtrack.facade.AuthFacade;
import com.jhonatan.gymtrack.factory.ApiResponseFactory;
import com.jhonatan.gymtrack.security.AuthToken;
import com.jhonatan.gymtrack.service.IAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IAuthService service;
    private final AuthFacade facade;

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthToken>> login (@RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        AuthToken token = service.login(loginRequestDTO);
        return new ResponseEntity<>(ApiResponseFactory.success(token), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<APIResponse<AuthToken>> register(@RequestBody @Valid RegisterRequestDTO request) {

        AuthToken token = facade.registerAndLogin(request);
        return new ResponseEntity<>(ApiResponseFactory.success(token), HttpStatus.OK);

    }
}
