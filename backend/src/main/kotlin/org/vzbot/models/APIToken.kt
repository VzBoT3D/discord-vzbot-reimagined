package org.vzbot.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.statix.Model
import org.statix.ModelIgnore

object APITokens: IntIdTable("api_tokens") {
    val token = varchar("api_token", 36)
    val initiator = long("api_token_initiator")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}

@Model
class APIToken(id: EntityID<Int>): Entity<Int>(id) {
    companion object: EntityClass<Int, APIToken>(APITokens)

    var token by APITokens.token
    var initiator by APITokens.initiator

    @ModelIgnore
    val createdAt by APITokens.createdAt
}