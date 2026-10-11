package ru.mirea.examgitgud.model;

/**
 * Тема дисциплины, к которой относятся задания (например, «Производная»).
 *
 * @param id         идентификатор
 * @param examId     идентификатор экзамена
 * @param name       название темы
 * @param taskNumber номер вопроса экзаменационного билета, к которому относится тема
 */
public record Topic(Long id, Long examId, String name, int taskNumber) {
}
