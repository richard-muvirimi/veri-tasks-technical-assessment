package com.tyganeutronics.tasks.demo.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/health")
@CrossOrigin(origins = ["*"])
class HealthController {

    @GetMapping
    fun health(): ResponseEntity<Map<String, Any>> {
        val healthResponse = mapOf(
            "status" to "UP",
            "timestamp" to LocalDateTime.now().toString(),
            "service" to "tasks-api",
            "version" to "0.0.1-SNAPSHOT"
        )
        return ResponseEntity.ok(healthResponse)
    }

    @GetMapping("/status")
    fun status(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("status" to "UP"))
    }
}