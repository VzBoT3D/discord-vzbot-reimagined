package org.vzbot.io

import com.zellerfeld.zellerbotapi.util.EnvType

enum class EnvVariables(
    val type: EnvType,
    val default: String? = null,
    val requiresNonEmpty: Boolean = true,
    val isHidden: Boolean = true,
) {
    TOKEN(EnvType.STRING),
    LOG_CHANNEL(EnvType.LONG),
    ADMIN_ROLE(EnvType.STRING),
    SERIAL_CATEGORY(EnvType.LONG),
    VZ_TEAM_ROLE(EnvType.LONG),
    SERIAL_ANNOUNCEMENT_CHANNEL(EnvType.LONG),

    SERIAL_BASE_PLATE_LOCATION(EnvType.STRING),
    SERIAL_NUMBER_PLATES_LOCATION(EnvType.STRING),

    DB_USER(EnvType.STRING),
    DB_PASSWORD(EnvType.STRING, requiresNonEmpty = false),
    DB_HOST(EnvType.STRING),
    DB_DATABASE(EnvType.STRING),
    DB_PORT(EnvType.STRING),
}