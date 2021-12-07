package com.kisswe.gatekeeper.filter

import com.kisswe.gatekeeper.config.AuthHeaderConfig
import com.kisswe.gatekeeper.model.User
import com.kisswe.gatekeeper.service.JwtService
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthFilter(
    private val jwtService: JwtService,
    private val authHeaderConfig: AuthHeaderConfig,
): AbstractGatewayFilterFactory<Any>() {
    private val log = LoggerFactory.getLogger(AuthFilter::class.java)

    override fun apply(config: Any?) = GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
        log.info("AuthFilter baseMessage: {}")
        log.info("AuthFilter Start: {}", exchange.request);
        val token = (exchange.request.headers["Authorization"]?.get(0) ?: "")
            .replace("Bearer ", "")
        log.info("Authorization Token = $token")
        val user: User
            try {
            user = jwtService.verifyAuthToken(token)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        val request = exchange.request.mutate().header(authHeaderConfig.userId, user.id.toString()).build()
        chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable {
            log.info("AuthFilter End: {}", exchange.response)
        })
    }
}
