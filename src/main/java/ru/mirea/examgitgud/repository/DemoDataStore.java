package ru.mirea.examgitgud.repository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.mirea.examgitgud.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Хранилище данных в памяти для MVP и демонстрации.
 *
 * <p>В рабочей версии заменяется репозиториями Spring Data JPA поверх PostgreSQL
 * (таблицы описаны в логической модели БД). Интерфейс методов при этом не меняется.</p>
 */
@Repository
public class DemoDataStore {

    private final Map<Long, Exam> exams = new LinkedHashMap<>();
    private final Map<Long, Topic> topics = new LinkedHashMap<>();
    private final Map<Long, Task> tasks = new LinkedHashMap<>();
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private final Map<Long, Attempt> attempts = new ConcurrentHashMap<>();
    private final AtomicLong attemptSeq = new AtomicLong(1);

    /**
     * Заполняет хранилище демонстрационными данными: одна дисциплина,
     * две темы, пять заданий и два пользователя.
     */
    public DemoDataStore() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usersByEmail.put("anna@example.com", new User(1L, "anna@example.com",
                encoder.encode("Anna2026!"), "Анна Смирнова", Role.STUDENT));
        usersByEmail.put("teacher@example.com", new User(2L, "teacher@example.com",
                encoder.encode("Teach2026!"), "Орлов Сергей Николаевич", Role.TEACHER));

        exams.put(1L, new Exam(1L, "Математический анализ", 90, 40));
        topics.put(1L, new Topic(1L, 1L, "Пределы", 1));
        topics.put(2L, new Topic(2L, 1L, "Производная", 2));

        addTask(new Task(1L, 1L, "Вычислите предел lim (x -> 2) (x^2 - 4) / (x - 2).", "4",
                "При x != 2 дробь равна x + 2, поэтому предел равен 4.", 1, 1, TaskStatus.PUBLISHED));
        addTask(new Task(2L, 1L, "Вычислите предел lim (x -> 0) sin(5x) / x.", "5",
                "По первому замечательному пределу sin(5x) / x -> 5.", 1, 1, TaskStatus.PUBLISHED));
        addTask(new Task(3L, 1L, "Вычислите предел lim (x -> бесконечность) (x + 1) / (2x).", "0,5",
                "Делим числитель и знаменатель на x: (1 + 1/x) / 2 -> 0,5.", 1, 1, TaskStatus.PUBLISHED));
        addTask(new Task(4L, 2L, "Найдите значение производной функции f(x) = x^2 в точке x0 = 3.", "6",
                "f'(x) = 2x, f'(3) = 6.", 2, 1, TaskStatus.PUBLISHED));
        addTask(new Task(5L, 2L, "Найдите значение производной функции f(x) = 5x - 4 в точке x0 = 10.", "5",
                "Производная линейной функции равна угловому коэффициенту: 5.", 1, 1, TaskStatus.PUBLISHED));
    }

    private void addTask(Task task) {
        tasks.put(task.id(), task);
    }

    /** @param email логин @return пользователь, если найден */
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email == null ? "" : email.trim().toLowerCase()));
    }

    /** @param id идентификатор @return экзамен, если найден */
    public Optional<Exam> findExam(Long id) { return Optional.ofNullable(exams.get(id)); }

    /** @return все темы */
    public Collection<Topic> findTopics() { return topics.values(); }

    /** @param id идентификатор @return задание, если найдено */
    public Optional<Task> findTask(Long id) { return Optional.ofNullable(tasks.get(id)); }

    /**
     * Возвращает опубликованные задания, при необходимости отфильтрованные по теме.
     *
     * @param topicId идентификатор темы или {@code null} для всех тем
     * @return список опубликованных заданий
     */
    public List<Task> findPublishedTasks(Long topicId) {
        return tasks.values().stream()
                .filter(t -> t.status() == TaskStatus.PUBLISHED)
                .filter(t -> topicId == null || t.topicId().equals(topicId))
                .toList();
    }

    /** @return следующий идентификатор попытки */
    public Long nextAttemptId() { return attemptSeq.getAndIncrement(); }

    /** @param attempt сохраняемая попытка */
    public void saveAttempt(Attempt attempt) { attempts.put(attempt.getId(), attempt); }

    /** @param id идентификатор @return попытка, если найдена */
    public Optional<Attempt> findAttempt(Long id) { return Optional.ofNullable(attempts.get(id)); }
}
