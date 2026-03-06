package org.vzbot.plugins

import io.ktor.server.application.*
import org.vzbot.middleware.APITokenMiddleware
import statix.org.Middlewares

fun Application.configureMiddlewares() {
    install(Middlewares) {
        middleware = APITokenMiddleware()
    }
}