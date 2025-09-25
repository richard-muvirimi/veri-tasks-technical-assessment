package com.tyganeutronics.tasks.demo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class TaskRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 255, message = "Title must be less than 255 characters")
    val title: String,

    val description: String? = null,

    val completed: Boolean = false
)

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val completed: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class TaskUpdateRequest(
    val title: String? = null,
    val description: String? = null,
    val completed: Boolean? = null
)