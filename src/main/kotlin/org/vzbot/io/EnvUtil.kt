package org.vzbot.io

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.github.oshai.kotlinlogging.KotlinLogging
import com.zellerfeld.zellerbotapi.util.EnvType

val mode: ProgramMode = ProgramMode.valueOf(System.getenv("VZ_MODE"))

class Env {
    private var dotenv: Dotenv? = null
    private val logger = KotlinLogging.logger { }

    init {
        if (mode == ProgramMode.DEV) {
            dotenv = dotenv()
        }

        val entries = EnvVariables.entries
        logger.debug("${entries.size} env entries were supplied")

        val setEntries = getAllKeys()

        if (setEntries.size > entries.size) {
            logger.warn("${setEntries.size} were entries found, but only ${entries.size} is required")

            for (setEntry in setEntries) {
                if (entries.firstOrNull { it.name == setEntry } == null) {
                    logger.warn("$setEntry is set but not required by local definitions!")
                }
            }
        }

        for (value in entries) {
            val envValue =
                value.default
                    ?: (getOrNull(value.name) ?: error("Key ${value.name} not found in environment"))

            when (value.type) {
                EnvType.INT -> {
                    if (envValue.toIntOrNull() == null) error("Can't cast ${value.name} to INT")
                }
                EnvType.LONG -> {
                    if (envValue.toLongOrNull() == null) error("Can't cast ${value.name} to LONG")
                }
                EnvType.NUMBER -> {
                    if (envValue.toDoubleOrNull() == null) error("Can't cast ${value.name} to NUMBER")
                }
                EnvType.BOOLEAN -> {
                    if (envValue.toBooleanStrictOrNull() == null) error("Can't cast ${value.name} to BOOLEAN")
                }
                else -> {}
            }

            if (value.requiresNonEmpty && envValue.isEmpty()) {
                error("Value ${value.name} requires non empty value, but a empty value has been supplied!")
            }
        }
    }

    private fun getAllKeys(): List<String> =
        if (mode == ProgramMode.DEV) {
            dotenv!!.entries().map { it.key }
        } else {
            System.getenv().map { it.key }
        }

    private fun getOrNull(s: String): String? {
        return if (mode == ProgramMode.DEV) {
            if (dotenv!!.entries().none { it.key == s }) {
                return null
            }

            dotenv!!.get(s)
        } else {
            if (System.getenv(s) == null) {
                return null
            }
            System.getenv(s)
        }
    }

    operator fun get(variable: EnvVariables): String {
        return getOrNull(variable.name) ?: run {
            if (variable.default != null) {
                return variable.default
            }
            error("${variable.name} no found in .env")
        }
    }
}

val env = Env()

enum class ProgramMode {
    DEPLOY,
    DEV,
}
