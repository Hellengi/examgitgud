package ru.mirea.examgitgud.model;

/**
 * Экзамен из справочника, например «Математический анализ».
 *
 * @param id              идентификатор
 * @param name            название экзамена
 * @param durationMinutes длительность экзамена в минутах (используется таймером)
 * @param maxScore        максимальный балл за экзамен по балльно-рейтинговой системе
 */
public record Exam(Long id, String name, int durationMinutes, int maxScore) {
}
