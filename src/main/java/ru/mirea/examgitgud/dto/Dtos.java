package ru.mirea.examgitgud.dto;

import ru.mirea.examgitgud.model.Attempt;
import ru.mirea.examgitgud.model.Task;

import java.time.Instant;
import java.util.List;

/**
 * Объекты передачи данных REST API. Эталонный ответ задания
 * никогда не передаётся клиенту до проверки.
 */
public final class Dtos {

    private Dtos() {
    }

    /** Запрос на вход. @param email логин @param password пароль */
    public record LoginRequest(String email, String password) { }

    /** Ответ на успешный вход. @param id идентификатор @param fullName ФИО @param role роль */
    public record LoginResponse(Long id, String fullName, String role) { }

    /** Сообщение об ошибке. @param error текст ошибки */
    public record ErrorResponse(String error) { }

    /** Ответ студента на задание. @param answer значение ответа */
    public record AnswerRequest(String answer) { }

    /**
     * Результат проверки ответа в режиме тренировки.
     *
     * @param correct       верен ли ответ
     * @param correctAnswer эталонный ответ (показывается после проверки)
     * @param solution      разбор решения
     */
    public record CheckResult(boolean correct, String correctAnswer, String solution) { }

    /**
     * Задание без эталонного ответа.
     *
     * @param id         идентификатор
     * @param topicId    тема
     * @param statement  условие
     * @param difficulty сложность
     */
    public record TaskView(Long id, Long topicId, String statement, int difficulty) {
        /** @param t задание @return представление для клиента */
        public static TaskView of(Task t) {
            return new TaskView(t.id(), t.topicId(), t.statement(), t.difficulty());
        }
    }

    /** Запрос на старт попытки. @param userId студент @param examId экзамен */
    public record StartAttemptRequest(Long userId, Long examId) { }

    /**
     * Состояние попытки для клиента.
     *
     * @param id        идентификатор
     * @param examName  название экзамена
     * @param status    статус
     * @param deadline  время автозавершения
     * @param score     балл (после завершения)
     * @param maxScore  максимальный балл
     * @param tasks     задания варианта
     */
    public record AttemptView(Long id, String examName, String status, Instant deadline,
                              int score, int maxScore, List<TaskView> tasks) {
        /** @param a попытка @return представление для клиента */
        public static AttemptView of(Attempt a) {
            int max = a.getTasks().stream().mapToInt(Task::score).sum();
            return new AttemptView(a.getId(), a.getExam().name(), a.getStatus().name(), a.getDeadline(),
                    a.getScore(), max, a.getTasks().stream().map(TaskView::of).toList());
        }
    }
}
