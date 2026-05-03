package com.jhonatan.gymtrack.controller;


import com.jhonatan.gymtrack.dto.APIResponse;
import com.jhonatan.gymtrack.dto.authDto.AuthenticatedUserDTO;
import com.jhonatan.gymtrack.dto.authDto.LoginRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.RegisterRequestDTO;
import com.jhonatan.gymtrack.facade.AuthFacade;
import com.jhonatan.gymtrack.factory.ApiResponseFactory;
import com.jhonatan.gymtrack.security.AuthToken;
import com.jhonatan.gymtrack.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and account registration")
public class AuthController {

    private final IAuthService service;
    private final AuthFacade facade;

    // ------------------------------------------------------------------ //
    //  LOGIN
    // ------------------------------------------------------------------ //
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
    public ResponseEntity<APIResponse<AuthenticatedUserDTO>> login (
            @RequestBody @Valid LoginRequestDTO loginRequestDTO,
            HttpServletResponse response) {

        AuthenticatedUserDTO user = service.login(loginRequestDTO, response);
        return new ResponseEntity<>(ApiResponseFactory.success(user), HttpStatus.OK);
    }

    // ------------------------------------------------------------------ //
    //  REGISTER
    // ------------------------------------------------------------------ //

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
    public ResponseEntity<APIResponse<AuthenticatedUserDTO>> register(
            @RequestBody @Valid RegisterRequestDTO request,
            HttpServletResponse response) {

        AuthenticatedUserDTO user = facade.registerAndLogin(request, response);
        return new ResponseEntity<>(ApiResponseFactory.success(user), HttpStatus.OK);

    }

    // ------------------------------------------------------------------ //
    //  ME — verifica sessão ativa
    // ------------------------------------------------------------------ //

    @Operation(
            summary = "Get current authenticated user",
            description = "Reads the HttpOnly cookie and returns the current user's data. " +
                    "Use this endpoint on app startup to check if the session is still valid."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session calid, user data returned"),
            @ApiResponse(responseCode = "401", description = "No valid session / cookie expired")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<APIResponse<AuthenticatedUserDTO>> me(Authentication authentication) {
        //o spring ja validou o jwt do cookie antes de chegar aqui
        // authentication.getName() retorna o email (no caso é o subject do token)
        String email = authentication.getName();
        AuthenticatedUserDTO user = service.getCurrentUser(email);
        return ResponseEntity.ok(ApiResponseFactory.success(user));
    }


    // ------------------------------------------------------------------ //
    //  LOGOUT
    // ------------------------------------------------------------------ //

    @Operation(
            summary = "Logout current user",
            description = "Invalidates the session by instructing the browser to delete the JWT cookie."
    )
    @ApiResponse(responseCode = "204", description = "Logout successful")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false) //mesma coisa do login
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        return ResponseEntity.noContent().build();
    }

}
