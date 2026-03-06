package org.vzbot.discord.util

import org.vzbot.io.BotLogger
import org.vzbot.io.EnvVariables
import org.vzbot.io.env
import java.io.File

fun fetchFilesForSerial(serial: Long): List<File> {
    val serialBaseSTL = File(env[EnvVariables.VZ_SERIAL_BASE_PLATE_LOCATION], "plate.stl")
    val serialNumberSTL = File(env[EnvVariables.VZ_SERIAL_NUMBER_PLATES_LOCATION], "${serial}.stl")

    println(serialBaseSTL.absolutePath)
    println(serialNumberSTL.absoluteFile)

    try {
        val fileExists1 = serialBaseSTL.exists()
        val fileExists2 = serialNumberSTL.exists()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    val fileExists1 = serialBaseSTL.exists()
    val fileExists2 = serialNumberSTL.exists()

    if (fileExists1 && fileExists2) {
        return listOf(serialBaseSTL, serialNumberSTL)
    } else {
        BotLogger.logError( "The serial application for ID: $serial was just finished, but one of the files was not found! Please check this.")
    }
    return listOf()
}