package com.tyganeutronics.tasks.demo.controller

import com.tyganeutronics.tasks.demo.dto.AuthResponse
import com.tyganeutronics.tasks.demo.dto.LoginRequest
import com.tyganeutronics.tasks.demo.dto.RegisterRequest
import com.tyganeutronics.tasks.demo.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(AuthController::class.java)
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<AuthResponse> {
        logger.info("Register attempt for username={}", registerRequest.username)
        return try {
            val response = authService.register(registerRequest)
            logger.info("User registered: {}", response.username)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.warn("Registration failed for username={}: {}", registerRequest.username, e.message)
            logger.debug("Registration exception:", e)
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        logger.info("Login attempt for username={}", loginRequest.username)
        return try {
            val response = authService.login(loginRequest)
            logger.info("Login successful for username={}", response.username)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.warn("Login failed for username={}: {}", loginRequest.username, e.message)
            logger.debug("Login exception:", e)
            ResponseEntity.badRequest().build()
        }
    }
}