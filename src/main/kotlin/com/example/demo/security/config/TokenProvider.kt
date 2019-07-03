package com.example.demo.security.config

import com.example.demo.security.model.Constant.ACCESS_TOKEN_VALIDITY_SECONDS
import com.example.demo.security.model.Constant.AUTHORITIES_KEY
import com.example.demo.security.model.Constant.HEADER_STRING
import com.example.demo.security.model.Constant.SIGNING_KEY
import com.example.demo.security.model.Constant.TOKEN_PREFIX
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import javax.crypto.spec.SecretKeySpec
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.xml.bind.DatatypeConverter


class TokenProvider {
    companion object {
        @JvmStatic
        fun generateToken(username: String, userRole: String): String {
            val secretKey = generateSecretKey()
            val authorityClaims = HashMap<String, Any>()
            authorityClaims[AUTHORITIES_KEY] = userRole
            val token = Jwts.builder().setClaims(authorityClaims)
                    .setSubject(username)
                    .setExpiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact()

            return TOKEN_PREFIX + " " + token
        }

        fun generateSecretKey(): Key {
            val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SIGNING_KEY)
            val signatureAlgorithm = SignatureAlgorithm.HS256

            return SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)
        }

        fun addTokenToResponse(res: HttpServletResponse, token: String) {
            res.addHeader(HEADER_STRING, token)
        }
    }


    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER_STRING)
        if (isTokenValid(token)) {
            val user = getSubjectOfToken(token)
            val tokenClaims = getTokenClaims(token)
            val tokenAuthorities = getTokenAuthorities(tokenClaims)

            if (user != null) {
                return UsernamePasswordAuthenticationToken(user, null, tokenAuthorities)
            }
        }
        return null
    }

    private fun isTokenValid(token: String?): Boolean {
        return token != null && token.toLowerCase().contains(TOKEN_PREFIX.toLowerCase())
    }

    private fun getSubjectOfToken(token: String): String? {
        return getTokenClaims(token)
                .subject
    }

    private fun getTokenClaims(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SIGNING_KEY))
                .parseClaimsJws(getTokenWithoutBearerPrefix(token))
                .body
    }

    fun getTokenWithoutBearerPrefix(token: String): String {
        return token.replace(TOKEN_PREFIX, "")
    }

    private fun getTokenAuthorities(claims: Claims): Collection<GrantedAuthority> {
        return Arrays.asList(*claims[AUTHORITIES_KEY]
                .toString()
                .split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                .stream()
                .map(::SimpleGrantedAuthority)
                .collect(Collectors.toList());
    }

}