package ru.mirea.examgitgud.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Конфигурация системных часов. Вынесена в отдельный бин,
 * чтобы в тестах можно было проверить автозавершение по таймеру.
 */
@Configuration
public class ClockConfig {

    /** @return системные часы в часовом поясе UTC */
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
