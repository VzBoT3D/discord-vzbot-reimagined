package org.vzbot.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.io.EnvVariables
import org.vzbot.io.env
import org.vzbot.models.APITokens
import org.vzbot.models.SerialNumbers
import org.vzbot.models.SerialTickets

fun Application.configureDatabases() {
    val config = HikariConfig().apply {
        username = env[EnvVariables.DB_USER]
        password = env[EnvVariables.DB_PASSWORD]
        jdbcUrl = "jdbc:mariadb://${env[EnvVariables.DB_HOST]}:${env[EnvVariables.DB_PORT]}/${env[EnvVariables.DB_DATABASE]}"
        driverClassName = "org.mariadb.jdbc.Driver"
    }

    val source = HikariDataSource(config)

    Database.connect(source)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(APITokens, SerialTickets, SerialNumbers)
    }
}
