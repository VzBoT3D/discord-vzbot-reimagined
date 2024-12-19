package org.vzbot.io

import com.zellerfeld.zellerbotapi.util.EnvType

enum class EnvVariables(
    val type: EnvType,
    val default: String? = null,
    val requiresNonEmpty: Boolean = true,
    val isHidden: Boolean = true,
) {
    VZ_TOKEN(EnvType.STRING),
    VZ_LOG_CHANNEL(EnvType.LONG),
    VZ_ADMIN_ROLE(EnvType.STRING),
    VZ_SERIAL_CATEGORY(EnvType.LONG),
    VZ_TEAM_ROLE(EnvType.LONG),
    VZ_SERIAL_ANNOUNCEMENT_CHANNEL(EnvType.LONG),

    VZ_SERIAL_BASE_PLATE_LOCATION(EnvType.STRING),
    VZ_SERIAL_NUMBER_PLATES_LOCATION(EnvType.STRING),

    VZ_WEBSITE_URL(EnvType.STRING),

    VZ_DB_USER(EnvType.STRING),
    VZ_DB_PASSWORD(EnvType.STRING, requiresNonEmpty = true),
    VZ_DB_HOST(EnvType.STRING),
    VZ_DB_DATABASE(EnvType.STRING),
    VZ_DB_PORT(EnvType.NUMBER),
}