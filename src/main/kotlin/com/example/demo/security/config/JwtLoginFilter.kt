package com.example.demo.security.config

import com.example.demo.security.config.TokenProvider.Companion.addTokenToResponse
import com.example.demo.security.config.TokenProvider.Companion.generateToken
import com.example.demo.security.model.AccountInfo
import com.example.demo.security.model.UserCredentials
import com.fasterxml.jackson.databind.ObjectMapper
import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import lombok.extern.slf4j.Slf4j
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Slf4j
class JwtLoginFilter(url: String, authManager: AuthenticationManager) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(url)) {

    init {
        authenticationManager = authManager
    }

    @Throws(IOException::class)
    override fun attemptAuthentication(req: HttpServletRequest, res: HttpServletResponse): Authentication {

        val creds = ObjectMapper()
                .readValue(req.inputStream, UserCredentials::class.java)

        return authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(creds.username, creds.password))
    }

    override fun successfulAuthentication(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain,
                                          auth: Authentication) {

        val userDetails = auth.principal as UserDetails
        var userRole: String = ""

        for (authority in userDetails.authorities) {

            userRole = authority.authority
            if (userRole != null)
                break
        }

        val token = generateToken(auth.name, userRole)
        addTokenToResponse(res, token)
        addAccountInfoToResponse(res, auth.name, userRole)
    }

    private fun addAccountInfoToResponse(response: HttpServletResponse, username: String, role: String) {
        response.contentType = "application/json"

            val accountInfoInJson = ObjectMapper().writeValueAsString(AccountInfo(username, role))

            response.writer
                    .write(accountInfoInJson)
    }
}
