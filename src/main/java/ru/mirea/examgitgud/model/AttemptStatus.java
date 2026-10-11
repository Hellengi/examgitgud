package ru.mirea.examgitgud.model;

/**
 * Состояние попытки прохождения пробного экзамена.
 */
public enum AttemptStatus {
    /** Попытка идёт, таймер запущен. */
    IN_PROGRESS,
    /** Студент завершил попытку сам. */
    FINISHED,
    /** Попытка завершена автоматически по истечении времени. */
    TIME_EXPIRED
}
