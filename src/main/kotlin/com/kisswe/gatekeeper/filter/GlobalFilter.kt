package com.kisswe.gatekeeper.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalFilter: AbstractGatewayFilterFactory<Any>() {
    private val log = LoggerFactory.getLogger(GlobalFilter::class.java)

    override fun apply(config: Any?) = GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
        log.info("GlobalFilter baseMessage: {}")
        log.info("GlobalFilter Start: {}", exchange.request);
        chain.filter(exchange).then(Mono.fromRunnable { log.info("GlobalFilter End: {}", exchange.response)})
    }
}
