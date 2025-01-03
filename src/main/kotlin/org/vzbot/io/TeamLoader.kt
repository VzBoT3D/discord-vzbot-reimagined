package org.vzbot.io

import com.zellerfeld.zellerbotapi.ZellerBot
import com.zellerfeld.zellerbotapi.util.scheduling.runRepeating
import io.ktor.http.*
import org.vzbot.models.author
import org.vzbot.models.generated.BlogAuthorModelDTO
import org.vzbot.models.generated.toModel
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object TeamLoader {
    private val teamMembers = mutableMapOf<String, List<BlogAuthorModelDTO>>()

    fun getTeamMembers() = teamMembers.toMap()

    private fun loadTeamMembers() {
        val teamRole = ZellerBot.getRole(env[EnvVariables.VZ_TEAM_ROLE]) ?: return
        val contributorRole = ZellerBot.getRole(env[EnvVariables.VZ_CONTRIBUTOR_ROLE]) ?: return
        val moderatorRole = ZellerBot.getRole(env[EnvVariables.VZ_MODERATOR_ROLE]) ?: return

        ZellerBot.mainGuild!!.findMembersWithRoles(teamRole).onSuccess {
            teamMembers["team"] = it.map { it.author().toModel() }
        }

        ZellerBot.mainGuild!!.findMembersWithRoles(moderatorRole).onSuccess {
            teamMembers["moderators"] = it
                .filter { !it.roles.contains(teamRole) }
                .map { it.author().toModel() }
        }

        ZellerBot.mainGuild!!.findMembersWithRoles(contributorRole).onSuccess {
            teamMembers["contributor"] = it
                .filter { !it.roles.contains(teamRole) }
                .filter { !it.roles.contains(moderatorRole) }
                .map { it.author().toModel() }
        }
    }

    fun startScheduler() {
        runRepeating(0.seconds, 1.minutes) {
            loadTeamMembers()
        }
    }
}