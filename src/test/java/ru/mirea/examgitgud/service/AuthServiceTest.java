package ru.mirea.examgitgud.service;

import org.junit.jupiter.api.Test;
import ru.mirea.examgitgud.repository.DemoDataStore;

import static org.junit.jupiter.api.Assertions.*;

/** Тесты аутентификации. */
class AuthServiceTest {

    private final AuthService auth = new AuthService(new DemoDataStore());

    @Test
    void correctCredentialsAccepted() {
        assertTrue(auth.login("anna@example.com", "Anna2026!").isPresent());
    }

    @Test
    void wrongPasswordRejected() {
        assertTrue(auth.login("anna@example.com", "wrong").isEmpty());
    }

    @Test
    void unknownUserRejected() {
        assertTrue(auth.login("nobody@example.com", "Anna2026!").isEmpty());
    }
}
