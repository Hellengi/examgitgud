package ru.mirea.examgitgud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.examgitgud.dto.Dtos.*;
import ru.mirea.examgitgud.service.AuthService;

/**
 * REST-контроллер авторизации. Базовый путь {@code /api/auth}.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /** @param authService сервис аутентификации */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Вход по e-mail и паролю.
     *
     * @param request учётные данные
     * @return 200 и данные пользователя либо 401 с сообщением «Ошибка аутентификации»
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request.email(), request.password())
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(
                        new LoginResponse(u.getId(), u.getFullName(), u.getRole().name())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Ошибка аутентификации")));
    }
}
