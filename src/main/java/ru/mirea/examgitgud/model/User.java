package ru.mirea.examgitgud.model;

/**
 * Учётная запись пользователя платформы.
 *
 * <p>Пароль хранится только в виде bcrypt-хэша (требование 4.9 матрицы требований).</p>
 */
public class User {

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final String fullName;
    private final Role role;
    private boolean blocked;

    /**
     * Создаёт пользователя.
     *
     * @param id           идентификатор
     * @param email        адрес электронной почты (логин)
     * @param passwordHash bcrypt-хэш пароля
     * @param fullName     ФИО пользователя
     * @param role         роль пользователя
     */
    public User(Long id, String email, String passwordHash, String fullName, Role role) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
    }

    /** @return идентификатор пользователя */
    public Long getId() { return id; }

    /** @return адрес электронной почты */
    public String getEmail() { return email; }

    /** @return bcrypt-хэш пароля */
    public String getPasswordHash() { return passwordHash; }

    /** @return ФИО пользователя */
    public String getFullName() { return fullName; }

    /** @return роль пользователя */
    public Role getRole() { return role; }

    /** @return {@code true}, если учётная запись заблокирована администратором */
    public boolean isBlocked() { return blocked; }

    /**
     * Блокирует или разблокирует учётную запись (требование 3.2).
     *
     * @param blocked новое состояние блокировки
     */
    public void setBlocked(boolean blocked) { this.blocked = blocked; }
}
