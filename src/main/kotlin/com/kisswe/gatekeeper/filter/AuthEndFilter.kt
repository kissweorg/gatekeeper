package com.kisswe.gatekeeper.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthEndFilter: AbstractGatewayFilterFactory<Any>() {
    private val log = LoggerFactory.getLogger(AuthFilter::class.java)

    override fun apply(config: Any?) = GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
        log.info("AuthEndFilter baseMessage: {}")
        log.info("AuthEndFilter Start: {}", exchange.request);
        chain.filter(exchange).then(Mono.fromRunnable { log.info("AuthEndFilter End: {}", exchange.response)})
    }
}