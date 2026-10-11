package ru.mirea.examgitgud.service;

import org.springframework.stereotype.Service;
import ru.mirea.examgitgud.model.*;
import ru.mirea.examgitgud.repository.DemoDataStore;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Бизнес-логика пробного экзамена (требование 1.6): формирование варианта,
 * сохранение ответов, завершение попытки вручную или по таймеру.
 */
@Service
public class AttemptService {

    private final DemoDataStore store;
    private final ScoreCalculator calculator;
    private final Clock clock;

    /**
     * @param store      хранилище данных
     * @param calculator калькулятор баллов
     * @param clock      источник времени (в тестах подменяется фиксированным)
     */
    public AttemptService(DemoDataStore store, ScoreCalculator calculator, Clock clock) {
        this.store = store;
        this.calculator = calculator;
        this.clock = clock;
    }

    /**
     * Формирует вариант по программе дисциплины и запускает таймер.
     * В MVP вариант состоит из всех опубликованных заданий экзамена.
     *
     * @param userId идентификатор студента
     * @param examId идентификатор экзамена
     * @return созданная попытка со статусом {@link AttemptStatus#IN_PROGRESS}
     * @throws NoSuchElementException если экзамен не найден
     */
    public Attempt start(Long userId, Long examId) {
        Exam exam = store.findExam(examId)
                .orElseThrow(() -> new NoSuchElementException("Экзамен не найден: " + examId));
        List<Task> variant = store.findPublishedTasks(null);
        Attempt attempt = new Attempt(store.nextAttemptId(), userId, exam, variant, Instant.now(clock));
        store.saveAttempt(attempt);
        return attempt;
    }

    /**
     * Сохраняет ответ студента. Если время уже истекло, попытка закрывается,
     * а ответ не принимается.
     *
     * @param attemptId идентификатор попытки
     * @param taskId    идентификатор задания
     * @param value     ответ студента
     * @return актуальное состояние попытки
     * @throws IllegalStateException если попытка уже завершена
     */
    public Attempt saveAnswer(Long attemptId, Long taskId, String value) {
        Attempt attempt = get(attemptId);
        if (expireIfNeeded(attempt)) {
            return attempt;
        }
        ensureInProgress(attempt);
        attempt.putAnswer(new Answer(taskId, value, Instant.now(clock)));
        return attempt;
    }

    /**
     * Завершает попытку по кнопке «Завершить» и подсчитывает баллы.
     *
     * @param attemptId идентификатор попытки
     * @return завершённая попытка с баллом
     */
    public Attempt finish(Long attemptId) {
        Attempt attempt = get(attemptId);
        if (expireIfNeeded(attempt)) {
            return attempt;
        }
        ensureInProgress(attempt);
        attempt.close(calculator.calculate(attempt.getTasks(), attempt.getAnswers()),
                AttemptStatus.FINISHED, Instant.now(clock));
        return attempt;
    }

    /**
     * Возвращает попытку, предварительно проверив, не истекло ли время.
     *
     * @param attemptId идентификатор попытки
     * @return попытка
     */
    public Attempt get(Long attemptId) {
        Attempt attempt = store.findAttempt(attemptId)
                .orElseThrow(() -> new NoSuchElementException("Попытка не найдена: " + attemptId));
        expireIfNeeded(attempt);
        return attempt;
    }

    private boolean expireIfNeeded(Attempt attempt) {
        if (attempt.getStatus() == AttemptStatus.IN_PROGRESS && !Instant.now(clock).isBefore(attempt.getDeadline())) {
            attempt.close(calculator.calculate(attempt.getTasks(), attempt.getAnswers()),
                    AttemptStatus.TIME_EXPIRED, attempt.getDeadline());
            return true;
        }
        return attempt.getStatus() == AttemptStatus.TIME_EXPIRED;
    }

    private void ensureInProgress(Attempt attempt) {
        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Попытка уже завершена");
        }
    }
}
