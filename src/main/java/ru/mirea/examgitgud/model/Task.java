package ru.mirea.examgitgud.model;

/**
 * Задание из банка заданий с кратким ответом.
 *
 * @param id            идентификатор
 * @param topicId       идентификатор темы
 * @param statement     условие задания
 * @param correctAnswer эталонный ответ
 * @param solution      текст разбора решения
 * @param difficulty    сложность от 1 до 3
 * @param score         количество первичных баллов за верный ответ
 * @param status        статус публикации
 */
public record Task(Long id, Long topicId, String statement, String correctAnswer,
                   String solution, int difficulty, int score, TaskStatus status) {
}
