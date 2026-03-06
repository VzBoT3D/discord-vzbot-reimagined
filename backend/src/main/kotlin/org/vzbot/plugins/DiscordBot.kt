package org.vzbot.plugins

import com.ktbot.api.KtBot
import com.ktbot.api.util.Token
import com.ktbot.api.util.scheduling.ExceptionHandler
import org.vzbot.discord.App
import org.vzbot.io.BotLogger
import org.vzbot.io.EnvVariables
import org.vzbot.io.TeamLoader
import org.vzbot.io.env

fun configureBot() {
    val token = env[EnvVariables.VZ_TOKEN]
    KtBot.registerApplication(App())

    KtBot.onReady {
        BotLogger.logInfo("Bot is online!")
        TeamLoader.startScheduler()
    }

    ExceptionHandler.onException { t, e ->
        e.printStackTrace()
        BotLogger.logError("There was an error with the bot in thread ${t.name}: ${e.message}")
    }

    KtBot.startBot(Token(token), {}, {
        error("Failed to start bot: ${it.reason}")
    }, loadEnv = false)
}