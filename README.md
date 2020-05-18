Тестовое задание

Система бронирования билетов в социальном кинотеатре. 
Просмотр информации и покупка билетов.

Считаем, что пользователь предварительно прошел регистрацию, авторизацию и т.п., 
например по номеру телефона или почте.

Две роли пользователя:
1. Клиент.
2. Администратор.

Клиенты могут быть двух категорий:
1. Обычная.
2. Социальная-1 (20% скидка).
3. Социальная-2 (50% скидка).
4. Социальная-3 (100% скидка).

Сервис позволяет клиенту:
1. Просмотреть список сеансов.
2. Забронировать билет на сеанс.
3. Просмотреть информацию о забронированных местах.
4. Отменить бронирование.
5. Просмотреть историю посещенных сеансов.

Сервис позволяет администратору:
1. Добавить сеанс.
2. Перенести сеанс.
3. Отменить сеанс.

Социальная категория имеет приоритет в покупке билетов.
Чем больше индекс социальной категории, тем больше скидка на цену билета.

Обычная категория может забронировать билет только за 15 минут до начала сеанса.
Если заполняемость зала ниже определенного значения, возможность покупки билета для обычной категории может открыться раньше.
Кинотеатр в любом случае должен получить с зала не меньше определенного % прибыли при полной посадке.

Должна быть возможность отключения социальных льгот, 
то есть уравнивания категорий граждан между собой.

Требования к реализации:
1. Исходный код на Github.
2. Реализован на Kotlin (jdk 8).
3. Должен собираться, запускаться, иметь тестовые данные.
4. Не должны требоваться установка отдельных программ для запуска.
5. Должен быть описан README.MD.
6. Коммиты должны быть на русском языке.

---

Реализовано два сервиса и контроллеры для них 

Написаны тесты для репозиториев и сервисов

---

Логическая схема данных

Категория пользователя (1) -> (n) Пользователь (n) <- (1) Тип пользователя (Клиент/Администратор)
   
Пользователь (1) -> (n) Бронирование места (n) <- (1) Сеанс

---
Пользователи социальной группы могут бронировать билеты в любое время  

Бронирование билетов для обычных пользователей  
В сущности сеанс есть поле minimumProfit - минимальная сумма выгоды с сеанса  
Если текущий доход с сеанса меньше этой суммы, то бронировать билеты могут все кто угодно  
Как только минимальная сумма будет достигнута - обычные пользователи бронируют не ранее чем за 15 минут до начала сеанса   

В сервисе администратора есть методы включения и отключения льгот  
При отключении льгот - пользователи социальной группы могут покупать билеты в любое время, но за полную стоимость  
