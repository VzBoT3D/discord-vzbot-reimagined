package org.vzbot.discord.commands

import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordSubCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommandOption
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DSubCommand
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.restrictions.TeamMemberRestriction
import org.vzbot.io.prettyEmbed
import org.vzbot.models.BlogPost
import org.vzbot.models.BlogPosts
import org.vzbot.models.generated.toModel
import java.awt.Color
import java.nio.file.Files
import kotlin.io.path.writeText

/**
 * @author Devin Fritz
 */
@DCommand("blog", "manage the blogs on the vz web page")
class BlogCommand: DiscordCommand() {

    @DSubCommand("creates a new blog post, with you being the author")
    class Create: DiscordSubCommand() {

        @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
        override fun execute(actionSender: ActionSender) {

        }
    }

    @DSubCommand("deletes an existing blog post.")
    class Delete: DiscordSubCommand() {

        @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
        override fun execute(actionSender: ActionSender) {
            super.execute(actionSender)
        }
    }

    @DSubCommand("lists all of the existing blog posts")
    class List: DiscordSubCommand() {

        @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
        override fun execute(actionSender: ActionSender) {
            val posts = transaction { BlogPost.all().map { it.toModel() }.toList() }

            runBlocking {
                posts.forEach { it.with { author() } }
            }

            val embed = prettyEmbed("All posts", if (posts.isEmpty()) "There are no posts yet :(. Be the first to create one!" else "Use /blog view <id> to get a detailed view of a blog post.", Color.GREEN)

            posts.forEach {
                embed.addField("Post: ${it.id} by ${it.author!!.name}", "Created at: ${it.createdAt}", false)
            }

            actionSender.respondEmbed(embed.build(), true)
        }
    }

    @DSubCommand("view a specific blog post")
    class View: DiscordSubCommand() {

        @DCommandOption("the id of the blog post")
        var blogId: Int = 0

        @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
        override fun execute(actionSender: ActionSender) {
            val blogPost = transaction { BlogPost.findById(blogId) }
            if (blogPost == null) {
                actionSender.respondText("There is no blog post with the given id", true)
                return
            }

            val markdownFile = Files.createTempFile("post-$blogId", ".md")
            markdownFile.writeText(transaction { blogPost.content })

            actionSender.respondFile(markdownFile.toFile())
        }
    }
}