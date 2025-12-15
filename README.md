Запускать проект: 
mvn compile exec:java
Ktor-сервер начнёт слушать http://localhost:8080.

# ClickHouse + Ktor + Kotlin

Проект использует Kotlin и Ktor для создания веб-сервера, интегрированного с ClickHouse.

### Локальный запуск (с Docker)

1. Убедитесь, что установлены:
    - JDK 11+
    - Maven
    - Docker
    - Docker Compose

2. Запустите ClickHouse и приложение: 
docker-compose up -d 
mvn compile exec:java
3. Сервер будет доступен на:  
   🔗 [http://localhost:8080](http://localhost:8080)

### Kubernetes

Для развёртывания в Kubernetes:
bash kubectl apply -f k8s/

---

## 2. `docker-compose.yml` — локальный запуск
yaml docker-compose.yml version: '3.8'
services: clickhouse: image: yandex/clickhouse-server:22.8 container_name: clickhouse-server ports: - "8123:8123" - "9000:9000" environment: - CLICKHOUSE_USER=default - CLICKHOUSE_PASSWORD=clickhouse - CLICKHOUSE_DB=ktor_db volumes: - clickhouse_data:/var/lib/clickhouse - ./init.sql:/docker-entrypoint-initdb.d/init.sql
app: build: . ports: - "8080:8080" depends_on: - clickhouse environment: - CLICKHOUSE_JDBC_URL=jdbc:clickhouse://clickhouse:8123/ktor_db 
# Запуск через Maven (для разработки) # command: mvn compile exec:java
volumes: clickhouse_data:

> ⚠️ Если вы не будете собирать образ приложения — `app` можно временно убрать из `docker-compose`, пока не добавим `Dockerfile`.
