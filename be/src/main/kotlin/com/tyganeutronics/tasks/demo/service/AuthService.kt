package com.tyganeutronics.tasks.demo.service

import com.tyganeutronics.tasks.demo.dto.AuthResponse
import com.tyganeutronics.tasks.demo.dto.LoginRequest
import com.tyganeutronics.tasks.demo.dto.RegisterRequest
import com.tyganeutronics.tasks.demo.entity.User
import com.tyganeutronics.tasks.demo.repository.UserRepository
import com.tyganeutronics.tasks.demo.util.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(AuthService::class.java)
    }

    fun register(registerRequest: RegisterRequest): AuthResponse {
        if (userRepository.existsByUsername(registerRequest.username)) {
            throw RuntimeException("Username is already taken!")
        }

        if (userRepository.existsByEmail(registerRequest.email)) {
            throw RuntimeException("Email is already in use!")
        }

        val user = User(
            username = registerRequest.username,
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password)
        )

        val savedUser = userRepository.save(user)
        val userDetails = userDetailsService.loadUserByUsername(savedUser.username)
        val jwt = jwtUtil.generateToken(userDetails)

        return AuthResponse(
            id = savedUser.id,
            token = jwt,
            username = savedUser.username,
            email = savedUser.email
        )
    }

    fun login(loginRequest: LoginRequest): AuthResponse {
        // Check if user exists and password comparison
        var user = userRepository.findByUsername(loginRequest.username)
            .orElseThrow { BadCredentialsException("Invalid username or password") }

        val foundUser = user
        logger.debug("Found user: {}", foundUser.username)
        logger.debug("Stored password hash: {}", foundUser.password)
        logger.debug("Password encoder matches: {}", passwordEncoder.matches(loginRequest.password, foundUser.password))
        
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
        } catch (e: Exception) {
            logger.warn("Authentication failed for user {}: {}", loginRequest.username, e.message)
            throw BadCredentialsException("Invalid username or password", e)
        }

        val userDetails = userDetailsService.loadUserByUsername(loginRequest.username)
        user = userRepository.findByUsername(loginRequest.username)
            .orElseThrow { UsernameNotFoundException("User not found") }
        
        val jwt = jwtUtil.generateToken(userDetails)

        logger.info("User logged in: {}", user.username)

        return AuthResponse(
            id = user.id,
            token = jwt,
            username = user.username,
            email = user.email
        )
    }
}