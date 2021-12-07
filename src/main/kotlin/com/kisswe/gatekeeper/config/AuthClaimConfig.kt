package com.kisswe.gatekeeper.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "auth.claim")
data class AuthClaimConfig(
    val issuer: String,
    val audience: String,
)
