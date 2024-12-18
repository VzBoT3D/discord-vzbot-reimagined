package org.vzbot.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.io.EnvVariables
import org.vzbot.io.env
import org.vzbot.models.*

fun Application.configureDatabases() {
    val config = HikariConfig().apply {
        username = env[EnvVariables.VZ_DB_USER]
        password = env[EnvVariables.VZ_DB_PASSWORD]
        jdbcUrl = "jdbc:mariadb://${env[EnvVariables.VZ_DB_HOST]}:${env[EnvVariables.VZ_DB_PORT]}/${env[EnvVariables.VZ_DB_DATABASE]}"
        driverClassName = "org.mariadb.jdbc.Driver"
    }

    val source = HikariDataSource(config)

    Database.connect(source)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            APITokens,
            SerialTickets,
            SerialNumbers,
            Printers,
            PrinterProfiles)
    }
}
