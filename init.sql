-- Создаём базу данных, если ещё не существует
CREATE DATABASE IF NOT EXISTS ktor_db;
-- Создаём таблицу, если ещё не существует
CREATE TABLE IF NOT EXISTS ktor_db.clicks (
    id UUID DEFAULT generateUUIDv4(),
    page String,
    user_id String,
    timestamp DateTime DEFAULT now()
    ) ENGINE = MergeTree()
    ORDER BY (timestamp);