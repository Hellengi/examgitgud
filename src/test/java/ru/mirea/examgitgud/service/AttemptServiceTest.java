package ru.mirea.examgitgud.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mirea.examgitgud.model.Attempt;
import ru.mirea.examgitgud.model.AttemptStatus;
import ru.mirea.examgitgud.repository.DemoDataStore;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты пробного экзамена: подсчёт баллов и автозавершение по таймеру.
 */
class AttemptServiceTest {

    /** Управляемые часы, чтобы «перематывать» время в тесте. */
    static class MutableClock extends Clock {
        Instant now = Instant.parse("2026-04-01T09:00:00Z");
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    private MutableClock clock;
    private AttemptService service;

    @BeforeEach
    void setUp() {
        clock = new MutableClock();
        service = new AttemptService(new DemoDataStore(), new ScoreCalculator(new AnswerChecker()), clock);
    }

    @Test
    @DisplayName("Запуск формирует вариант и ставит таймер на 90 минут")
    void startCreatesVariantWithDeadline() {
        Attempt a = service.start(1L, 1L);
        assertEquals(5, a.getTasks().size());
        assertEquals(Duration.ofMinutes(90), Duration.between(a.getStartedAt(), a.getDeadline()));
        assertEquals(AttemptStatus.IN_PROGRESS, a.getStatus());
    }

    @Test
    @DisplayName("Завершение считает баллы только за верные ответы")
    void finishCountsScore() {
        Attempt a = service.start(1L, 1L);
        service.saveAnswer(a.getId(), 1L, "4");
        service.saveAnswer(a.getId(), 3L, "0.5");
        service.saveAnswer(a.getId(), 4L, "9");
        Attempt result = service.finish(a.getId());
        assertEquals(2, result.getScore());
        assertEquals(AttemptStatus.FINISHED, result.getStatus());
    }

    @Test
    @DisplayName("По истечении времени попытка завершается автоматически")
    void attemptExpiresAutomatically() {
        Attempt a = service.start(1L, 1L);
        service.saveAnswer(a.getId(), 1L, "4");
        clock.now = clock.now.plus(Duration.ofMinutes(91));
        Attempt result = service.saveAnswer(a.getId(), 2L, "5");
        assertEquals(AttemptStatus.TIME_EXPIRED, result.getStatus());
        assertEquals(1, result.getScore());
        assertFalse(result.getAnswers().containsKey(2L));
    }

    @Test
    @DisplayName("Повторное завершение запрещено")
    void cannotFinishTwice() {
        Attempt a = service.start(1L, 1L);
        service.finish(a.getId());
        assertThrows(IllegalStateException.class, () -> service.finish(a.getId()));
    }
}
