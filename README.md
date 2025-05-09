
# Transfer API

Backend-сервис для управления пользователями, аккаунтами и переводами средств.

---

## 🚀 Быстрый старт

### 🔧 Требования:
- Java 17+
- Maven 3.8+
- Docker (для Redis и PostgreSQL)

### 📦 Сборка и запуск:

mvn clean install  
mvn spring-boot:run

🔐 Swagger UI:

http://localhost:8080/swagger-ui/index.html

Используемые технологии

* Java 17	Основной язык
* Spring Boot	Фреймворк
* Spring Security	Аутентификация с JWT
* Spring Data JPA	Работа с БД
* PostgreSQL	Основная база данных
* Redis	Кэширование
* MapStruct	Маппинг DTO ↔ Entity
* Testcontainers	Интеграционные тесты
* MockMvc + JUnit 5	Unit + API тестирование
* Liquibase	Миграции схемы
* Swagger (springdoc)	Документация API


✅ JWT (простая реализация)  
Аутентификация по email+password или phone+password.  
Claim только один — userId.

✅ Потокобезопасный перевод средств  
Перевод денег реализован с учётом блокировок (FOR UPDATE) и транзакций.
Защита от перевода самому себе, проверки баланса, потокобезопасное изменение.

✅ Кэширование
Кэш только на метод поиска пользователей, как единственный реально кэшируемый кейс.
Использован Redis.

✅ Увеличение баланса каждые 30 секунд  
Реализовано через планировщик внутри TransferService с ограничением 207% от начального депозита.

✅ REST API
Реализованы читаемые и единообразные endpoints с валидацией входных данных и централизованной обработкой ошибок.   
Поддержан поиск пользователей по фильтрам (name, email, phone, dateOfBirth) с пагинацией.   
Включено осмысленное логирование бизнес-операций и запросов.  

✅ Swagger  
Подключена конфигурация springdoc-openapi.  

🔐 Примеры запросов  
🔑 Получение токена:    
POST /api/v1/auth/email  
{  
"email": "test@example.com",  
"password": "password123"  
}  
🔐 Аутентифицированный запрос:

GET /api/v1/transfer/user/me  
Authorization: Bearer token   
  
🛠 Структура базы  
users (id, name, date_of_birth, password)  
account (user_id → user.id, balance)  
phone_data (user_id → user.id, phone)  
email_data (user_id → user.id, email)

✍️ Комментарий по выполнению
Проект выполнен за ~1.5 дня.
Сфокусировался на ключевых требованиях: 
безопасность, чистая архитектура, покрытие тестами, простота интеграции.

