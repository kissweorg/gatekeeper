package com.kisswe.gatekeeper.service

import com.kisswe.gatekeeper.config.AuthClaimConfig
import com.kisswe.gatekeeper.config.AuthSigningKeyConfig
import com.kisswe.gatekeeper.model.User
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.Key
import java.security.KeyStore

@Service
class JwtServiceImpl(
    private val authClaimConfig: AuthClaimConfig,
    authSigningKeyConfig: AuthSigningKeyConfig,
): JwtService {
    private val log = LoggerFactory.getLogger(JwtServiceImpl::class.java)
    private val signingPk: Key

    init {
        val keyStore = KeyStore.getInstance(authSigningKeyConfig.type)
        val fs = ClassLoader.getSystemResourceAsStream(authSigningKeyConfig.filename);
        keyStore.load(fs, authSigningKeyConfig.password.toCharArray())
        signingPk = keyStore.getCertificate(authSigningKeyConfig.alias).publicKey
    }

    // TODO: HANDLE THIS BETTER
    override fun verifyAuthToken(token: String): User {
        log.info("Trying to authenticate user with token = {}", token)
        val jwtBody = Jwts.parserBuilder()
            .requireIssuer(authClaimConfig.issuer)
            .requireAudience(authClaimConfig.audience)
            .setSigningKey(signingPk)
            .build()
            .parseClaimsJws(token)
        val user = User(id = jwtBody.body.subject.toLong())
        log.info("Authenticated user = {}", user)
        return user
    }
}
