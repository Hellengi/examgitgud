package ru.mirea.examgitgud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.examgitgud.dto.Dtos.*;
import ru.mirea.examgitgud.model.Topic;
import ru.mirea.examgitgud.repository.DemoDataStore;
import ru.mirea.examgitgud.service.AnswerChecker;

import java.util.Collection;
import java.util.List;

/**
 * REST-контроллер банка заданий и режима «Тренировка по темам» (требования 1.4, 1.5).
 */
@RestController
@RequestMapping("/api")
public class TaskController {

    private final DemoDataStore store;
    private final AnswerChecker checker;

    /**
     * @param store   хранилище заданий
     * @param checker проверка ответов
     */
    public TaskController(DemoDataStore store, AnswerChecker checker) {
        this.store = store;
        this.checker = checker;
    }

    /** @return список тем для фильтра */
    @GetMapping("/topics")
    public Collection<Topic> topics() {
        return store.findTopics();
    }

    /**
     * Список опубликованных заданий.
     *
     * @param topicId необязательный фильтр по теме
     * @return задания без эталонных ответов
     */
    @GetMapping("/tasks")
    public List<TaskView> tasks(@RequestParam(required = false) Long topicId) {
        return store.findPublishedTasks(topicId).stream().map(TaskView::of).toList();
    }

    /**
     * Проверяет ответ на задание и возвращает разбор.
     *
     * @param id      идентификатор задания
     * @param request ответ студента
     * @return результат проверки или 404, если задания нет
     */
    @PostMapping("/tasks/{id}/check")
    public ResponseEntity<CheckResult> check(@PathVariable Long id, @RequestBody AnswerRequest request) {
        return store.findTask(id)
                .map(t -> ResponseEntity.ok(new CheckResult(
                        checker.isCorrect(request.answer(), t.correctAnswer()), t.correctAnswer(), t.solution())))
                .orElse(ResponseEntity.notFound().build());
    }
}
