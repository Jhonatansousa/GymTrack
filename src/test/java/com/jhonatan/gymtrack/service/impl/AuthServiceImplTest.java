package com.jhonatan.gymtrack.service.impl;

import com.jhonatan.gymtrack.dto.authDto.LoginRequestDTO;
import com.jhonatan.gymtrack.dto.authDto.RegisterRequestDTO;
import com.jhonatan.gymtrack.entity.User;
import com.jhonatan.gymtrack.exception.InvalidCredentialsException;
import com.jhonatan.gymtrack.mapper.UserMapper;
import com.jhonatan.gymtrack.repository.UserRepo;
import com.jhonatan.gymtrack.security.AuthToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepo repo;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthServiceImpl authService;

    // ==========================================================================
    // LOGIN TESTS
    // ===========================================================================

    @Test
    @DisplayName("Should login successfully and return JWT token when credentials are valid")
    void shouldLoginSuccessfully_WhenCredentialsAreValid() {
        // ARRANGE
        LoginRequestDTO request = new LoginRequestDTO("jhonatan@test.com", "Password123!");

        // Criamos o usuário simulando como ele estaria no banco de dados (com a senha já criptografada)
        User userFromDb = User.builder()
                .id(UUID.randomUUID())
                .email("jhonatan@test.com")
                .password("encoded_password_from_db")
                .build();

        when(repo.findByEmail(request.getEmail())).thenReturn(userFromDb);
        // Simulamos que o PasswordEncoder comparou a senha em texto plano com a do banco e confirmou que batem
        when(encoder.matches(request.getPassword(), userFromDb.getPassword())).thenReturn(true);

        // ACT
        AuthToken result = authService.login(request);

        // ASSERT
        assertNotNull(result);
        assertNotNull(result.getToken()); // Garante que o TokenUtil.encodeToken funcionou e retornou a string JWT
        assertFalse(result.getToken().isEmpty());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when email is not found")
    void shouldThrowException_WhenEmailIsNotFound() {
        // ARRANGE
        LoginRequestDTO request = new LoginRequestDTO("wrong@test.com", "Password123!");

        when(repo.findByEmail(request.getEmail())).thenReturn(null);

        // ACT & ASSERT
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        // Se o usuário não existe, NUNCA deve chamar o encoder de senha (reduz processamento)
        verify(encoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password does not match")
    void shouldThrowException_WhenPasswordDoesNotMatch() {
        // ARRANGE
        LoginRequestDTO request = new LoginRequestDTO("jhonatan@test.com", "WrongPassword!");

        User userFromDb = User.builder()
                .email("jhonatan@test.com")
                .password("encoded_password_from_db")
                .build();

        when(repo.findByEmail(request.getEmail())).thenReturn(userFromDb);
        // Simulamos que o PasswordEncoder barrou a senha
        when(encoder.matches(request.getPassword(), userFromDb.getPassword())).thenReturn(false);

        // ACT & ASSERT
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    // =========================================================================
    // REGISTER TESTS
    // ========================================================================

    @Test
    @DisplayName("Should register user successfully and encrypt password")
    void shouldRegisterSuccessfully_WhenEmailIsNew() {
        // ARRANGE
        RegisterRequestDTO request = new RegisterRequestDTO("Jhonatan", "jhonatan@test.com", "RawPassword123!");

        User unmappedUser = User.builder().name("Jhonatan").email("jhonatan@test.com").build();

        when(repo.existsByEmail(request.getEmail())).thenReturn(false);
        when(mapper.toUser(request)).thenReturn(unmappedUser);
        // Quando pedir para encriptar, retorna um hash fictício
        when(encoder.encode(request.getPassword())).thenReturn("hashed_password_xyz");

        // ACT
        authService.register(request);

        // ASSERT
        // Verificamos se o mtodo save foi chamado passando um usuário que tem a senha encriptada,
        // e não a senha normal.. MT importante pra segurança.
        verify(repo, times(1)).save(argThat(user ->
                user.getPassword().equals("hashed_password_xyz") &&
                        user.getEmail().equals("jhonatan@test.com")
        ));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when trying to register an existing email")
    void shouldThrowException_WhenRegisteringExistingEmail() {
        // ARRANGE
        RegisterRequestDTO request = new RegisterRequestDTO("Jhonatan", "existente@test.com", "RawPassword123!");

        when(repo.existsByEmail(request.getEmail())).thenReturn(true);

        // ACT & ASSERT
        assertThrows(InvalidCredentialsException.class, () -> authService.register(request));

        // Garante que não tentou converter, nem encriptar, nem salvar no banco
        verify(mapper, never()).toUser(any());
        verify(encoder, never()).encode(any());
        verify(repo, never()).save(any());
    }
}