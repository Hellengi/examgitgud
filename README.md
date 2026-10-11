# ExamGitGud

Платформа для подготовки к вузовским экзаменам и зачётам. Учебный проект по дисциплине «Системная и программная инженерия», РТУ МИРЭА, группа ИКБО-71-23.

Команда: Акрамов М.М. (руководитель, аналитик, технический писатель, тестировщик), Муссобоев К.Р. (разработчик, дизайнер).

## Стек
- Back-end: Java 17, Spring Boot 3, Maven
- Front-end: React + TypeScript (в этом репозитории — кликабельный прототип на HTML/JS в `src/main/resources/static`)
- БД: PostgreSQL 16 (в MVP — хранилище в памяти `DemoDataStore`), Redis
- Тесты: JUnit 5 (TDD), Cucumber (BDD), Selenium IDE (UI)

## Запуск
Нужны JDK 17+ и Maven 3.9+ (или IntelliJ IDEA).

```bash
mvn spring-boot:run
```
Откройте http://localhost:8080. Демо-пользователь (студент): `anna@example.com` / `Anna2026!`.

## Тесты
```bash
mvn test
```
Запускаются модульные тесты (`src/test/java/.../service`) и BDD-сценарии (`src/test/resources/features`). HTML-отчёт Cucumber: `target/cucumber-report.html`.

Все команды ниже выполняются в PowerShell из папки проекта при запущенном приложении (`mvn spring-boot:run` в отдельном окне).

- UI-тесты (Selenium IDE): `npm install -g selenium-side-runner`, затем `selenium-side-runner selenium\ExamGitGud.side`
- API-тесты (Postman): `npx newman run postman\ExamGitGud.postman_collection.json`
- Нагрузочный тест (JMeter): `jmeter -n -t jmeter\ExamGitGud-load.jmx -l jmeter\result.jtl -e -o jmeter\report`
- Производительность и доступность: `npx lighthouse http://localhost:8080/ --only-categories=performance,accessibility --view`

## Документация
- Документация разработчика: `mvn javadoc:javadoc` → `target/reports/apidocs/index.html` (в старых версиях плагина — `target/site/apidocs`).
- Документация пользователя: GitHub Wiki (исходники страниц — `docs/wiki`).

## Ветки
- `main` — стабильная версия;
- `dev` — интеграция;
- `feature/<задача>` — разработка отдельных функций, слияние в `dev` через Pull Request.

������� �������: 90 �����, �������������� �������.
