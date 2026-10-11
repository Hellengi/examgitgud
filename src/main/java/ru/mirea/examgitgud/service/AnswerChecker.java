package ru.mirea.examgitgud.service;

import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Автоматическая проверка краткого ответа студента (требование 1.4).
 *
 * <p>Перед сравнением ответ нормализуется: обрезаются пробелы, регистр приводится
 * к нижнему, десятичная запятая заменяется точкой, лишние нули после точки
 * отбрасываются. Поэтому ответы «0,5», « 0.50 » и «0.5» считаются одинаковыми.</p>
 *
 * <p>Класс разработан по методологии TDD: тесты из {@code AnswerCheckerTest}
 * были написаны до реализации.</p>
 */
@Component
public class AnswerChecker {

    /**
     * Проверяет ответ студента.
     *
     * @param userAnswer    ответ, введённый студентом (может быть {@code null})
     * @param correctAnswer эталонный ответ из банка заданий
     * @return {@code true}, если ответы совпадают после нормализации
     * @throws IllegalArgumentException если эталонный ответ пустой
     */
    public boolean isCorrect(String userAnswer, String correctAnswer) {
        if (correctAnswer == null || correctAnswer.isBlank()) {
            throw new IllegalArgumentException("Эталонный ответ не задан");
        }
        if (userAnswer == null || userAnswer.isBlank()) {
            return false;
        }
        return normalize(userAnswer).equals(normalize(correctAnswer));
    }

    /**
     * Приводит ответ к каноническому виду.
     *
     * @param raw исходная строка
     * @return нормализованная строка
     */
    String normalize(String raw) {
        String value = raw.trim().toLowerCase(Locale.ROOT).replace(',', '.').replaceAll("\\s+", "");
        if (value.matches("-?\\d+\\.\\d+")) {
            value = value.replaceAll("0+$", "").replaceAll("\\.$", "");
        }
        if (value.equals("-0")) {
            value = "0";
        }
        return value;
    }
}
