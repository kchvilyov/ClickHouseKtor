-- Создаём таблицу, если ещё не существует
CREATE TABLE IF NOT EXISTS clicks (
    page String,
    user_id String,
    timestamp DateTime DEFAULT now()
) ENGINE = MergeTree()
ORDER BY (timestamp);