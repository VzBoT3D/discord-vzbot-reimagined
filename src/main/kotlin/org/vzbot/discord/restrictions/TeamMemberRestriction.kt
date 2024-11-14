package org.vzbot.discord.restrictions

import com.zellerfeld.zellerbotapi.ZellerBot
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import net.dv8tion.jda.api.entities.Member
import org.vzbot.io.EnvVariables
import org.vzbot.io.env

class TeamMemberRestriction {
    fun mustBeInTeam(member: ActionSender): Boolean {
        if (!member.member.isTeamMember()) {
            member.respondUnauthorized()
            return false
        }
        return true
    }
}

fun Member.isTeamMember(): Boolean {
    val vzTeamRole = ZellerBot.getRole(env[EnvVariables.VZ_TEAM_ROLE])
    return vzTeamRole in roles
}