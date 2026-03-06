package org.vzbot.io

import com.ktbot.api.KtBot
import java.awt.Color

object BotLogger {

    val infoChannel = KtBot.getTextChannel(env[EnvVariables.VZ_LOG_CHANNEL].toLong())

    init {
        if (infoChannel == null) {
            error("Info channel provided not found on guild!")
        }
    }

    fun logError(info: String) {
        infoChannel!!.sendMessageEmbeds(buildPrettyEmbed("Error", info, Color.RED)).queue()
    }

    fun logInfo(info: String) {
        infoChannel!!.sendMessageEmbeds(buildPrettyEmbed("Info", info, Color.ORANGE)).queue()
    }
}