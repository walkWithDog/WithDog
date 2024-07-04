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
    val accessExpiration = Date(now.time + accessTokenExpirationHour * 3600 * 1000)
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
        username : String,
        role: String
    ): String
    {
        return generateToken(subject, username, role, accessExpiration)
    }

    private fun generateToken(
        subject: String, username : String,
        role: String,
        accessExpiration: Date
    ): String
    {
        val claims: Claims = Jwts.claims()
            .add(mapOf("role" to role, "username" to username))
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