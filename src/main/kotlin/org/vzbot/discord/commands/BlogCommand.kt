package org.vzbot.discord.commands

import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.DiscordModal
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordSubCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommandOption
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DSubCommand
import com.zellerfeld.zellerbotapi.util.TempFile
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.restrictions.TeamMemberRestriction
import org.vzbot.io.EnvVariables
import org.vzbot.io.buildPrettyEmbed
import org.vzbot.io.env
import org.vzbot.io.prettyEmbed
import org.vzbot.models.BlogPost
import org.vzbot.models.BlogPosts
import org.vzbot.models.author
import org.vzbot.models.generated.toModel
import java.awt.Color
import java.nio.file.Files
import kotlin.io.path.readText
import kotlin.io.path.writeText

/**
 * @author Devin Fritz
 */
@DCommand("blog", "manage the blogs on the vz web page")
class BlogCommand: DiscordCommand() {

    @DSubCommand("creates a new blog post, with you being the author")
    class Create: DiscordSubCommand() {

        @DCommandOption("title of the blog post")
        lateinit var blogTitle: String

        @DCommandOption("whether to instantly publish this or not.")
        var publish: Boolean = false

        @DCommandOption("the markdown file of the post")
        lateinit var markdownFile: Message.Attachment

        @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
        override fun execute(actionSender: ActionSender) {
            if (!transaction { BlogPost.find { BlogPosts.title eq blogTitle }.empty() }) {
                actionSender.respondText("There is already another blog post with this title", true)
                return
            }

            if (markdownFile.fileExtension != "md") {
                actionSender.respondText("The provided file seems not to be a valid markdown file. Please only upload .md files.", true)
                return
            }

            val tempFile = Files.createTempFile("markdown", ".md")
            val path = markdownFile.proxy.downloadToPath(tempFile).get()
            val markdownContent = path.readText()

            val author = actionSender.member.author()

            val blogPost = transaction {
                BlogPost.new {
                    this.author = author
                    this.title = blogTitle
                    this.public = publish
                    this.content = markdownContent
                }
            }

            val id = transaction { blogPost.id.value }

            actionSender.respondEmbed(buildPrettyEmbed("Created", "You have created the blog post with the title $blogTitle and the id $id. Use /blog view <id> to view it or open: ${env[EnvVariables.VZ_WEBSITE_URL]}/blog/$id", Color.GREEN), true)
        }
    }

    @DSubCommand("deletes an existing blog post.")
    class Delete: DiscordSubCommand() {

        @DCommandOption("id of the blog post to delete")
        var blogID: Int = 0

        @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
        override fun execute(actionSender: ActionSender) {
            val blogPost = transaction { BlogPost.findById(blogID) }

            if (blogPost == null) {
                actionSender.respondText("There was no blog post with the given id found!", true)
                return
            }

            transaction { blogPost.delete() }
            actionSender.respondText("The post has been deleted", true)
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

    @DSubCommand("manage your authors profile page")
    class Profile: DiscordSubCommand() {

        @DSubCommand("edit your author details as you wish")
        class Edit: DiscordSubCommand() {

            @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
            override fun execute(actionSender: ActionSender) {
                val author = actionSender.member.author()
                val authorModel = author.toModel()

                val profilePictureInput = TextInput.create("profile_url", "Profile picture", TextInputStyle.PARAGRAPH).setValue(authorModel.profilePicture.ifBlank { "Avatar url" }).build()
                val nameInput = TextInput.create("name", "Display name", TextInputStyle.SHORT).setValue(authorModel.name).build()
                val descriptionInput = TextInput.create("description", "Author Description", TextInputStyle.PARAGRAPH).setValue(authorModel.description).build()

                val modal = DiscordModal("Edit your details", mutableListOf(ActionRow.of(profilePictureInput), ActionRow.of(nameInput), ActionRow.of(descriptionInput))) { modalSender, _, values ->
                    val profilePicture = values["profile_url"]!!.asString
                    val name = values["name"]!!.asString
                    val description = values["description"]!!.asString

                    transaction {
                        author.profilePicture = profilePicture
                        author.name = name
                        author.description = description
                    }

                    modalSender.respondEmbed(buildPrettyEmbed("Success", "Your profile has been edited. You can view your profile on ${env[EnvVariables.VZ_WEBSITE_URL]}authors/${authorModel.id}", Color.GREEN), true)
                }

                actionSender.respondModal(modal)
            }
        }

        @DSubCommand("get your current discord profile pictures avatar url")
        class Avatar: DiscordSubCommand() {
            @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
            override fun execute(actionSender: ActionSender) {
                actionSender.respondText(actionSender.member.user.avatarUrl ?: "No avatar found", true)
            }
        }
    }
}