package ru.mirea.examgitgud.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты проверки ответа. Написаны до реализации {@link AnswerChecker} (TDD).
 */
class AnswerCheckerTest {

    private final AnswerChecker checker = new AnswerChecker();

    @Test
    @DisplayName("Совпадающий целый ответ засчитывается")
    void exactAnswerIsCorrect() {
        assertTrue(checker.isCorrect("6", "6"));
    }

    @Test
    @DisplayName("Неверный ответ не засчитывается")
    void wrongAnswerIsIncorrect() {
        assertFalse(checker.isCorrect("7", "6"));
    }

    @Test
    @DisplayName("Десятичная запятая и точка равнозначны")
    void commaAndDotAreEqual() {
        assertTrue(checker.isCorrect("-0.5", "-0,5"));
    }

    @Test
    @DisplayName("Пробелы по краям и лишние нули игнорируются")
    void spacesAndTrailingZerosIgnored() {
        assertTrue(checker.isCorrect("  0,50 ", "0.5"));
    }

    @Test
    @DisplayName("Пустой ответ считается неверным")
    void emptyAnswerIsIncorrect() {
        assertFalse(checker.isCorrect("   ", "6"));
        assertFalse(checker.isCorrect(null, "6"));
    }

    @Test
    @DisplayName("Пустой эталон — ошибка данных задания")
    void blankReferenceThrows() {
        assertThrows(IllegalArgumentException.class, () -> checker.isCorrect("6", " "));
    }
}
