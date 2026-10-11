package ru.mirea.examgitgud.model;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Попытка прохождения пробного экзамена (варианта) студентом.
 *
 * <p>Хранит список заданий варианта, ответы студента и итоговый балл.
 * Ответ на одно и то же задание перезаписывается последним сохранённым значением.</p>
 */
public class Attempt {

    private final Long id;
    private final Long userId;
    private final Exam exam;
    private final List<Task> tasks;
    private final Instant startedAt;
    private final Map<Long, Answer> answers = new LinkedHashMap<>();
    private Instant finishedAt;
    private int score;
    private AttemptStatus status = AttemptStatus.IN_PROGRESS;

    /**
     * Создаёт новую попытку.
     *
     * @param id        идентификатор попытки
     * @param userId    идентификатор студента
     * @param exam      экзамен, по спецификации которого собран вариант
     * @param tasks     задания варианта в порядке следования
     * @param startedAt момент запуска таймера
     */
    public Attempt(Long id, Long userId, Exam exam, List<Task> tasks, Instant startedAt) {
        this.id = id;
        this.userId = userId;
        this.exam = exam;
        this.tasks = List.copyOf(tasks);
        this.startedAt = startedAt;
    }

    /**
     * Возвращает момент, когда попытка должна завершиться автоматически.
     *
     * @return время начала плюс длительность экзамена
     */
    public Instant getDeadline() {
        return startedAt.plusSeconds(exam.durationMinutes() * 60L);
    }

    /**
     * Сохраняет (или перезаписывает) ответ на задание.
     *
     * @param answer ответ студента
     */
    public void putAnswer(Answer answer) {
        answers.put(answer.taskId(), answer);
    }

    /**
     * Закрывает попытку и фиксирует результат.
     *
     * @param score      набранный балл
     * @param status     итоговый статус ({@link AttemptStatus#FINISHED} или {@link AttemptStatus#TIME_EXPIRED})
     * @param finishedAt момент завершения
     */
    public void close(int score, AttemptStatus status, Instant finishedAt) {
        this.score = score;
        this.status = status;
        this.finishedAt = finishedAt;
    }

    /** @return идентификатор попытки */
    public Long getId() { return id; }

    /** @return идентификатор студента */
    public Long getUserId() { return userId; }

    /** @return экзамен попытки */
    public Exam getExam() { return exam; }

    /** @return задания варианта */
    public List<Task> getTasks() { return tasks; }

    /** @return момент начала попытки */
    public Instant getStartedAt() { return startedAt; }

    /** @return ответы студента по идентификатору задания */
    public Map<Long, Answer> getAnswers() { return answers; }

    /** @return момент завершения или {@code null}, если попытка не завершена */
    public Instant getFinishedAt() { return finishedAt; }

    /** @return набранный первичный балл */
    public int getScore() { return score; }

    /** @return текущий статус попытки */
    public AttemptStatus getStatus() { return status; }
}
