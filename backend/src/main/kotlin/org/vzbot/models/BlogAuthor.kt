package org.vzbot.models

import net.dv8tion.jda.api.entities.Member
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.statix.HasMany
import org.statix.Model

object BlogAuthors: IntIdTable("blog_authors") {

    val discordID = long("discord_id")
    val name = varchar("author_name", 32)
    val profilePicture = varchar("profile_picture_url", 256)
    val description = text("author_description")

}

@Model
class BlogAuthor(id: EntityID<Int>): Entity<Int>(id) {
    companion object: EntityClass<Int, BlogAuthor>(BlogAuthors)

    @HasMany
    val posts by BlogPost referrersOn BlogPosts.author

    var discordID by BlogAuthors.discordID
    var name by BlogAuthors.name
    var profilePicture by BlogAuthors.profilePicture
    var description by BlogAuthors.description
}


fun Member.author(): BlogAuthor {
    if (isAuthor()) {
        return transaction { BlogAuthor.find { BlogAuthors.discordID eq idLong }.first() }
    }
    return createAuthor()
}

fun Member.isAuthor(): Boolean {
    return transaction { !BlogAuthor.find { BlogAuthors.discordID eq idLong }.empty() }
}

fun Member.createAuthor(): BlogAuthor {
    return transaction {
        BlogAuthor.new {
            this.discordID = idLong
            this.name = effectiveName
            this.profilePicture = user.avatarUrl ?: "https://github.com/VzBoT3D/VzBoT-Vz330/blob/master/Gallery/VzLoveAlt.png?raw=true"
            this.description = "VzBot Author"
        }
    }
}