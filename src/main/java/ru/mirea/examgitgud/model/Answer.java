package ru.mirea.examgitgud.model;

import java.time.Instant;

/**
 * Ответ студента на одно задание в рамках попытки.
 *
 * @param taskId     идентификатор задания
 * @param value      введённый студентом ответ
 * @param answeredAt момент сохранения ответа
 */
public record Answer(Long taskId, String value, Instant answeredAt) {
}
