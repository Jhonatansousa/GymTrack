package com.jhonatan.gymtrack.controller;


import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.authDto.LoginRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.RegisterRequestDTO;
import com.jhonatan.gymtrack.facade.AuthFacade;
import com.jhonatan.gymtrack.factory.ApiResponseFactory;
import com.jhonatan.gymtrack.security.AuthToken;
import com.jhonatan.gymtrack.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for user authentication and account registration")
public class AuthController {

    private final IAuthService service;
    private final AuthFacade facade;


    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user with email and password credentials. Returns a JWT token upon successful authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthToken>> login (@RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        AuthToken token = service.login(loginRequestDTO);
        return new ResponseEntity<>(ApiResponseFactory.success(token), HttpStatus.OK);
    }



    @Operation(
            summary = "Register new user",
            description = "Creates a new user account and automatically authenticates the user. " +
                    "Returns a JWT token upon successful registration."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered and authenticated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<APIResponse<AuthToken>> register(@RequestBody @Valid RegisterRequestDTO request) {

        AuthToken token = facade.registerAndLogin(request);
        return new ResponseEntity<>(ApiResponseFactory.success(token), HttpStatus.OK);

    }
}
