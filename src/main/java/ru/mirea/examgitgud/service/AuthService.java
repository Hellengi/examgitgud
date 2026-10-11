package ru.mirea.examgitgud.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mirea.examgitgud.model.User;
import ru.mirea.examgitgud.repository.DemoDataStore;

import java.util.Optional;

/**
 * Аутентификация пользователей по e-mail и паролю (требование 1.2).
 *
 * <p>В рабочей версии после успешного входа выдаётся JWT-токен со сроком жизни
 * 30 минут; в MVP возвращается сам пользователь.</p>
 */
@Service
public class AuthService {

    private final DemoDataStore store;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /** @param store хранилище пользователей */
    public AuthService(DemoDataStore store) {
        this.store = store;
    }

    /**
     * Проверяет учётные данные.
     *
     * @param email    логин пользователя
     * @param password пароль в открытом виде
     * @return пользователь, если e-mail и пароль верны и учётная запись не заблокирована
     */
    public Optional<User> login(String email, String password) {
        if (password == null) {
            return Optional.empty();
        }
        return store.findUserByEmail(email)
                .filter(u -> !u.isBlocked())
                .filter(u -> encoder.matches(password, u.getPasswordHash()));
    }
}
