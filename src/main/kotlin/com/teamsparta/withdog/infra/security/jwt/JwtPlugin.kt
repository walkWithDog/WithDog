package com.teamsparta.withdog.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class JwtPlugin(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour: Long
)
{
    var now = Date()
    val accessExpiration = Date(now.time + accessTokenExpirationHour)
    val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))


    fun validateToken(
        jwt: String
    ): Result<Jws<Claims>>
    {
        return kotlin.runCatching {

            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }

    fun generateAccessToken(
        subject: String,
        email: String,
        role: String
    ): String
    {
        return generateToken(subject, email, role, accessExpiration)
    }

    private fun generateToken(
        subject: String, email: String,
        role: String,
        accessExpiration: Date
    ): String
    {
        val claims: Claims = Jwts.claims()
            .add(mapOf("role" to role, "email" to email))
            .build()

        return Jwts.builder()
            .subject(subject)
            .issuer(issuer)
            .issuedAt(now)
            .expiration(accessExpiration)
            .claims(claims)
            .signWith(key)
            .compact()
    }
}