package com.kisswe.gatekeeper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@EnableConfigurationProperties
@SpringBootApplication
class GatekeeperApplication

fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "dev")
    runApplication<GatekeeperApplication>(*args)
}
