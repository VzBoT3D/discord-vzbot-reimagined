package org.vzbot.io

import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import java.awt.Color

fun ActionSender.respondError(error: String) {
    respondEmbed(buildPrettyEmbed("Error", error, Color.RED), true)
}

fun ActionSender.respondInfo(info: String) {
    respondEmbed(buildPrettyEmbed("Info", info, Color.ORANGE), true)
}

fun ActionSender.respondSuccess(info: String) {
    respondEmbed(buildPrettyEmbed("Success", info, Color.GREEN), true)
}