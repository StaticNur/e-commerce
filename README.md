# Russian Address Service

Этот проект предназначен для обработки, хранения и поиска адресных данных для России.

## Функциональность

- Парсинг архива "gar_xml.zip" с адресами России ([Актуальные данные для парсинга можно найти на официальном сайте ФИАС](https://fias.nalog.ru/Frontend).).
- Загрузка обработанных данных в базу данных PostgreSQL.
- Интеграция с Elasticsearch для быстрого поиска адресов.

## Структура проекта

- `parser-xml`: Микросервис для парсинга и загрузки данных в PostgreSQL.
- `fast-search-engine`: Микросервис для загрузки данных из PostgreSQL в Elasticsearch для быстрого поиска адресов.

## Технологии

- Java 21
- Spring Boot
- PostgreSQL
- Elasticsearch
- Docker
- Liquibase

## Установка и запуск

1. Клонируйте репозиторий:

```bash
git clone https://github.com/StaticNur/russian-address-service.git
