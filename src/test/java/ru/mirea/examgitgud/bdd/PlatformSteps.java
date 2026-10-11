package ru.mirea.examgitgud.bdd;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import ru.mirea.examgitgud.model.Attempt;
import ru.mirea.examgitgud.model.AttemptStatus;
import ru.mirea.examgitgud.model.Task;
import ru.mirea.examgitgud.model.User;
import ru.mirea.examgitgud.repository.DemoDataStore;
import ru.mirea.examgitgud.service.*;

import java.time.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Шаги BDD-сценариев платформы ExamGitGud. */
public class PlatformSteps {

    private final MutableClock clock = new MutableClock();
    private final DemoDataStore store = new DemoDataStore();
    private final AnswerChecker checker = new AnswerChecker();
    private final AttemptService attempts = new AttemptService(store, new ScoreCalculator(checker), clock);
    private final AuthService auth = new AuthService(store);
    private final Set<Long> mistakes = new HashSet<>();

    private Attempt attempt;
    private Task currentTask;
    private boolean lastCheck;
    private Optional<User> loginResult;
    private String message;

    static class MutableClock extends Clock {
        Instant now = Instant.parse("2026-04-01T09:00:00Z");
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    // ---------- Пробный экзамен ----------

    @Дано("студент авторизован и выбрал экзамен {string}")
    public void studentSelectedExam(String examName) {
        assertTrue(auth.login("anna@example.com", "Anna2026!").isPresent());
        assertEquals(examName, store.findExam(1L).orElseThrow().name());
    }

    @Когда("он нажимает {string}")
    public void pressStart(String button) {
        assertEquals("Начать пробный экзамен", button);
        attempt = attempts.start(1L, 1L);
    }

    @Тогда("система формирует вариант по программе дисциплины")
    public void variantCreated() {
        assertFalse(attempt.getTasks().isEmpty());
    }

    @И("запускается таймер на {int} минут")
    public void timerStarted(int minutes) {
        assertEquals(Duration.ofMinutes(minutes), Duration.between(attempt.getStartedAt(), attempt.getDeadline()));
    }

    @Дано("студент проходит пробный экзамен и ответил верно на {int} задание")
    public void studentInProgress(int correctCount) {
        attempt = attempts.start(1L, 1L);
        for (int i = 0; i < correctCount; i++) {
            Task t = attempt.getTasks().get(i);
            attempts.saveAnswer(attempt.getId(), t.id(), t.correctAnswer());
        }
    }

    @Когда("время на выполнение истекло")
    public void timeIsOver() {
        clock.now = clock.now.plus(Duration.ofMinutes(91));
        attempt = attempts.get(attempt.getId());
    }

    @Тогда("попытка завершается автоматически")
    public void attemptExpired() {
        assertEquals(AttemptStatus.TIME_EXPIRED, attempt.getStatus());
    }

    @И("система выводит {int} балл(а)(ов)")
    public void scoreShown(int score) {
        assertEquals(score, attempt.getScore());
    }

    // ---------- Тренировка по темам ----------

    @Дано("студент открыл задание по теме {string}")
    public void openTask(String topicName) {
        Long topicId = store.findTopics().stream().filter(t -> t.name().equals(topicName))
                .findFirst().orElseThrow().id();
        currentTask = store.findPublishedTasks(topicId).get(0);
    }

    @Когда("он вводит ответ {string}")
    public void enterAnswer(String answer) {
        lastCheck = checker.isCorrect(answer, currentTask.correctAnswer());
        if (!lastCheck) {
            mistakes.add(currentTask.id());
        }
    }

    @Тогда("система отмечает ответ как верный")
    public void markedCorrect() {
        assertTrue(lastCheck);
    }

    @Тогда("система отмечает ответ как неверный")
    public void markedWrong() {
        assertFalse(lastCheck);
    }

    @И("показывает пошаговый разбор решения")
    public void solutionShown() {
        assertFalse(currentTask.solution().isBlank());
    }

    @И("добавляет задание в раздел {string}")
    public void addedToMistakes(String section) {
        assertEquals("Работа над ошибками", section);
        assertTrue(mistakes.contains(currentTask.id()));
    }

    // ---------- Авторизация ----------

    @Дано("зарегистрированный пользователь {string} на странице входа")
    public void userOnLoginPage(String email) {
        assertTrue(store.findUserByEmail(email).isPresent());
    }

    @Когда("он вводит e-mail {string} и пароль {string}")
    public void login(String email, String password) {
        loginResult = auth.login(email, password);
        message = loginResult.isPresent() ? "Добро пожаловать" : "Ошибка аутентификации";
    }

    @Тогда("система выводит сообщение {string}")
    public void messageShown(String expected) {
        assertEquals(expected, message);
    }

    @И("доступ в личный кабинет не предоставляется")
    public void accessDenied() {
        assertTrue(loginResult.isEmpty());
    }

    @И("открывается личный кабинет студента")
    public void accessGranted() {
        assertTrue(loginResult.isPresent());
    }
}
