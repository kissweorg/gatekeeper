package com.kisswe.gatekeeper.service

import com.kisswe.gatekeeper.config.AuthSigningKeyConfig
import com.kisswe.gatekeeper.model.User
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.Key
import java.security.KeyStore

@Service
class JwtServiceImpl(authSigningKeyConfig: AuthSigningKeyConfig): JwtService {
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
            .requireIssuer("gatekeeper-dev")
            .requireAudience("bouncer-dev")
            .setSigningKey(signingPk)
            .build()
            .parseClaimsJws(token)
        val user = User(id = jwtBody.body.subject)
        log.info("Authenticated user = {}", user)
        return user
    }
}
