package org.vzbot.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.statix.BelongsTo
import org.statix.Model

object BlogPosts: IntIdTable("blog_posts") {

    val author = reference("blog_author_id", BlogAuthors.id)
    val content = mediumText("blog_content")
    val public = bool("blog_is_public")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

}

@Model
class BlogPost(id: EntityID<Int>): Entity<Int>(id) {
    companion object: EntityClass<Int, BlogPost>(BlogPosts)

    @BelongsTo
    var author by BlogAuthor referencedOn BlogPosts.author

    var content by BlogPosts.content
    var public by BlogPosts.public
    val createdAt by BlogPosts.createdAt

}