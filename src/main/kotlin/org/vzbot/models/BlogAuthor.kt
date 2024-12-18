package org.vzbot.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.statix.HasMany
import org.statix.Model

object BlogAuthors: IntIdTable("blog_authors") {

    val name = varchar("author_name", 32)
    val profilePicture = varchar("profile_picture_url", 256)
    val description = text("author_description")

}

@Model
class BlogAuthor(id: EntityID<Int>): Entity<Int>(id) {
    companion object: EntityClass<Int, BlogAuthor>(BlogAuthors)

    @HasMany
    val posts by BlogPost referrersOn BlogPosts.author

    var name by BlogAuthors.name
    var profilePicture by BlogAuthors.profilePicture
    var description by BlogAuthors.description

}