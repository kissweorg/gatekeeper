package com.kisswe.gatekeeper.service

import com.kisswe.gatekeeper.model.User

interface JwtService {
    fun verifyAuthToken(token: String): User
}