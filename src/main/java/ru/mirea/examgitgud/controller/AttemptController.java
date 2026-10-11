package ru.mirea.examgitgud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.examgitgud.dto.Dtos.*;
import ru.mirea.examgitgud.service.AttemptService;

import java.util.NoSuchElementException;

/**
 * REST-контроллер пробного экзамена. Базовый путь {@code /api/attempts}.
 */
@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService service;

    /** @param service сервис попыток */
    public AttemptController(AttemptService service) {
        this.service = service;
    }

    /**
     * Запускает пробный экзамен.
     *
     * @param request студент и экзамен
     * @return созданная попытка с вариантом и временем окончания
     */
    @PostMapping
    public AttemptView start(@RequestBody StartAttemptRequest request) {
        return AttemptView.of(service.start(request.userId(), request.examId()));
    }

    /**
     * Автосохранение ответа на задание.
     *
     * @param id      попытка
     * @param taskId  задание
     * @param request ответ
     * @return актуальное состояние попытки
     */
    @PutMapping("/{id}/answers/{taskId}")
    public AttemptView saveAnswer(@PathVariable Long id, @PathVariable Long taskId, @RequestBody AnswerRequest request) {
        return AttemptView.of(service.saveAnswer(id, taskId, request.answer()));
    }

    /**
     * Завершает попытку и подсчитывает баллы.
     *
     * @param id попытка
     * @return результат попытки
     */
    @PostMapping("/{id}/finish")
    public AttemptView finish(@PathVariable Long id) {
        return AttemptView.of(service.finish(id));
    }

    /**
     * @param id попытка
     * @return текущее состояние попытки
     */
    @GetMapping("/{id}")
    public AttemptView get(@PathVariable Long id) {
        return AttemptView.of(service.get(id));
    }

    /**
     * @param e исключение «не найдено»
     * @return ответ 404
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> notFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    /**
     * @param e исключение недопустимого состояния
     * @return ответ 409
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> conflict(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }
}
