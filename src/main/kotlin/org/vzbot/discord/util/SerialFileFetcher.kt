package org.vzbot.discord.util

import net.dv8tion.jda.api.utils.FileUpload
import org.vzbot.io.BotLogger
import org.vzbot.io.EnvVariables
import org.vzbot.io.env
import java.io.File

fun fetchFilesForSerial(serial: Long): List<File> {
    val serialBaseSTL = File(env[EnvVariables.VZ_SERIAL_BASE_PLATE_LOCATION], "plate.stl")
    val serialNumberSTL = File(env[EnvVariables.VZ_SERIAL_NUMBER_PLATES_LOCATION], "${serial}.stl")

    println(serialBaseSTL.absolutePath)
    println(serialNumberSTL.absoluteFile)

    if (serialBaseSTL.exists() && serialNumberSTL.exists()) {
        return listOf(serialBaseSTL, serialNumberSTL)
    } else {
        BotLogger.logError( "The serial application for ID: $serial was just finished, but one of the files was not found! Please check this.")
    }
    return listOf()
}