import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.DriverManager

fun main() {
    // Получаем URL ClickHouse из переменной окружения или используем локальный по умолчанию
    val jdbcUrl = System.getenv("CLICKHOUSE_JDBC_URL") ?: "jdbc:clickhouse://localhost:8123/default"
    val user = System.getenv("CLICKHOUSE_USER") ?: "default"
    val password = System.getenv("CLICKHOUSE_PASSWORD") ?: "clickhouse"

    // Создаём подключение (одно для простоты; в продакшене используйте пул)
    val connection by lazy {
        DriverManager.getConnection(jdbcUrl, user, password)
    }

    // Запускаем Ktor-сервер
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("/") {
                call.respondText("Hello from Ktor + ClickHouse! 🚀")
            }

            get("/health") {
                try {
                    val rs = connection.createStatement().executeQuery("SELECT version()")
                    if (rs.next()) {
                        call.respondText("OK: ClickHouse version = ${rs.getString(1)}")
                    } else {
                        call.respondText("OK: Connected to ClickHouse")
                    }
                } catch (e: Exception) {
                    call.respondText("ERROR: ${e.message}", status = io.ktor.http.HttpStatusCode.InternalServerError)
                }
            }

            get("/clicks") {
                try {
                    val result = connection.createStatement().use { stmt ->
                        val rs = stmt.executeQuery("SELECT page, user_id, timestamp FROM clicks LIMIT 10")
                        buildString {
                            appendLine("<h2>Recent clicks:</h2>")
                            appendLine("<ul>")
                            while (rs.next()) {
                                val page = rs.getString("page")
                                val user = rs.getString("user_id")
                                val time = rs.getString("timestamp")
                                appendLine("<li>$page — $user at $time</li>")
                            }
                            appendLine("</ul>")
                        }
                    }
                    call.respondText(result, contentType = io.ktor.http.ContentType.Text.Html)
                } catch (e: Exception) {
                    call.respondText("Database error: ${e.message}")
                }
            }

            get("/clicks/insert") {
                try {
                    connection.createStatement().use { stmt ->
                        stmt.execute("INSERT INTO clicks (page, user_id) VALUES ('/home', 'user_${System.currentTimeMillis() % 1000}')")
                    }
                    call.respondText("✅ Click inserted!")
                } catch (e: Exception) {
                    call.respondText("Insert failed: ${e.message}")
                }
            }
        }
    }.start(wait = true)
}