package com.tyganeutronics.tasks.demo.security

import com.tyganeutronics.tasks.demo.service.CustomUserDetailsService
import com.tyganeutronics.tasks.demo.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import org.slf4j.LoggerFactory

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    companion object {
        private val slf4j = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        slf4j.debug("Processing request: {}", request.requestURI)
        if (authHeader != null) {
            val preview = if (authHeader.length > 40) authHeader.substring(0, 40) + "..." else authHeader
            slf4j.debug("Authorization header preview: {}", preview)
        } else {
            slf4j.debug("Authorization header: null")
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No valid Authorization header found")
            chain.doFilter(request, response)
            return
        }

        try {
            val jwt = authHeader.substring(7)
            val jwtPreview = if (jwt.length > 40) jwt.substring(0, 40) + "..." else jwt
            slf4j.debug("Extracted JWT token preview: {}", jwtPreview)

            val username = jwtUtil.extractUsername(jwt)
            slf4j.debug("Extracted username from token: {}", username)

            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
                slf4j.debug("Loaded user details for: {}", userDetails.username)

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                    slf4j.info("Authentication set successfully for user: {}", username)
                } else {
                    slf4j.warn("Token validation failed for user: {}", username)
                }
            } else {
                slf4j.debug("Username is null or authentication already exists")
            }
        } catch (e: Exception) {
            slf4j.error("Cannot set user authentication: {}", e.message, e)
        }

        chain.doFilter(request, response)
    }
}