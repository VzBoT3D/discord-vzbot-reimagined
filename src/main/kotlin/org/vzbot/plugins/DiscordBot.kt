package org.vzbot.plugins

import com.zellerfeld.zellerbotapi.ZellerBot
import com.zellerfeld.zellerbotapi.util.Token
import com.zellerfeld.zellerbotapi.util.scheduling.ExceptionHandler
import org.vzbot.discord.App
import org.vzbot.io.BotLogger
import org.vzbot.io.EnvVariables
import org.vzbot.io.env

fun configureBot() {
    val token = env[EnvVariables.VZ_TOKEN]
    ZellerBot.registerApplication(App())

    ZellerBot.onReady {
        BotLogger.logInfo("Bot is online!")
    }

    ExceptionHandler.onException { t, e ->
        e.printStackTrace()
        BotLogger.logError("There was an error with the bot in thread ${t.name}: ${e.message}")
    }

    ZellerBot.startBot(Token(token), {}, {
        error("Failed to start bot: ${it.reason}")
    }, loadEnv = false)
}