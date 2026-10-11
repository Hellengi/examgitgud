package ru.mirea.examgitgud.service;

import org.springframework.stereotype.Component;
import ru.mirea.examgitgud.model.Answer;
import ru.mirea.examgitgud.model.Task;

import java.util.List;
import java.util.Map;

/**
 * Подсчёт первичных баллов за вариант пробного экзамена.
 */
@Component
public class ScoreCalculator {

    private final AnswerChecker checker;

    /**
     * @param checker компонент проверки отдельного ответа
     */
    public ScoreCalculator(AnswerChecker checker) {
        this.checker = checker;
    }

    /**
     * Считает сумму баллов за верно решённые задания.
     * Задания без ответа приносят 0 баллов.
     *
     * @param tasks   задания варианта
     * @param answers ответы студента по идентификатору задания
     * @return итоговый первичный балл (не больше суммы баллов заданий)
     */
    public int calculate(List<Task> tasks, Map<Long, Answer> answers) {
        int total = 0;
        for (Task task : tasks) {
            Answer answer = answers.get(task.id());
            if (answer != null && checker.isCorrect(answer.value(), task.correctAnswer())) {
                total += task.score();
            }
        }
        return total;
    }
}
