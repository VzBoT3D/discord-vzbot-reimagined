package org.vzbot.io

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

fun rawInfoEmbed(): EmbedBuilder {
    return prettyEmbed("Info", "", Color.GREEN)
}

fun infoEmbed(message: String): MessageEmbed {
    return prettyEmbed("Info", message, Color.GREEN).build()
}

fun buildPrettyEmbed(info: String, description: String, color: Color): MessageEmbed {
    return prettyEmbed(info, description, color).build()
}

fun prettyEmbed(info: String, description: String, color: Color): EmbedBuilder {
    return EmbedBuilder().apply {
        setTitle(info)
        setDescription(description)
        setColor(color)
        setFooter("VZBot - Official discord bot")
        setThumbnail("https://avatars.githubusercontent.com/u/90012124?s=400&u=3aa2a230843e9a8bd39c194e00c565d2d556081a&v=4")
    }
}