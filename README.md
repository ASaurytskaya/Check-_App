# Check Creator

## Описание:

Check Creator – это консольное приложение, реализующее функционал формирования чека в магазине и предназначено для создания и управления чеками. Оно поддерживает работу с товарами, дисконтными картами и дебетовыми картами. Приложение может быть запущено из командной строки с использованием аргументов для указания товаров, дисконтных карт и баланса дебетовых карт.

## Техничекие требования:

- Java 21 или выше
- PostgreSQL
- Gradle 8.5

## Сборка и запуск

### Сборка

Для компиляции проекта используйте следующую команду:

    ./gradlew clean build

### Запуск

Для запуска приложения используйте следующую команду:

    java -jar build/libs/clevertec-check-1.0.jar id-quantity discountCard=хххх balanceDebitCard=хххх  saveToFile=xxx datasource.url=ххх datasource.username=ххх datasource.password=ххх


* id-quantity – пары идентификаторов товаров и их количества. Например, 3-1 означает товар с идентификатором 3 в количестве 1. 
* discountCard=xxxx – номер дисконтной карты. Например, discountCard=1111. 
* balanceDebitCard=xxxx – баланс дебетовой карты. Например, balanceDebitCard=100.
* saveToFile=xxxx – включает относительный (от корневой директории проекта) путь + название файла с расширением.
* datasource.url=ххх, datasource.username=ххх, datasource.password=ххх – настройки подключения к базе данных.

Пример команды для запуска:

    java -cp out ru.clevertec.check.CheckRunner 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 saveToFile=src/main/resources/result.csv datasource.url=jdbc:postgresql://localhost:5430/check datasource.username=postgres datasource.password=postgres

## Исключения
Приложение может выбрасывать следующие исключения:
- BadRequestException – выбрасывается в случае некорректных аргументов. 
- NotEnoughMoneyException – выбрасывается в случае недостаточного баланса на дебетовой карте.
