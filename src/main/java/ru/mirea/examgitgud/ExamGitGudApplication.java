package ru.mirea.examgitgud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа серверной части платформы для подготовки к экзаменам ExamGitGud.
 *
 * <p>Приложение построено по трёхуровневой архитектуре: клиентская часть (React,
 * в MVP — статические страницы прототипа), сервер приложений на Spring Boot
 * и уровень данных (PostgreSQL; в MVP — хранилище в памяти).</p>
 *
 * @author Муссобоев К.Р.
 * @version 0.1.0
 */
@SpringBootApplication
public class ExamGitGudApplication {

    /**
     * Запускает встроенный веб-сервер и поднимает контекст Spring.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(ExamGitGudApplication.class, args);
    }
}
