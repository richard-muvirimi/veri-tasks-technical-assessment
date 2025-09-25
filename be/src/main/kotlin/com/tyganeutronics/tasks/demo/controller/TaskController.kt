package com.tyganeutronics.tasks.demo.controller

import com.tyganeutronics.tasks.demo.dto.TaskRequest
import com.tyganeutronics.tasks.demo.dto.TaskResponse
import com.tyganeutronics.tasks.demo.dto.TaskUpdateRequest
import com.tyganeutronics.tasks.demo.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(TaskController::class.java)
    }

    @GetMapping
    fun getAllTasks(authentication: Authentication): ResponseEntity<List<TaskResponse>> {
        logger.info("Get all tasks requested by user={}", authentication.name)
        return try {
            val tasks = taskService.getAllTasks(authentication.name)
            ResponseEntity.ok(tasks)
        } catch (e: Exception) {
            logger.warn("Failed to get tasks for user={}: {}", authentication.name, e.message)
            logger.debug("GetAllTasks exception:", e)
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long, authentication: Authentication): ResponseEntity<TaskResponse> {
        logger.info("Get task by id requested by user={}; id={}", authentication.name, id)
        return try {
            val task = taskService.getTask(authentication.name, id)
            ResponseEntity.ok(task)
        } catch (e: Exception) {
            logger.warn("Failed to get task id={} for user={}: {}", id, authentication.name, e.message)
            logger.debug("GetTaskById exception:", e)
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping
    fun createTask(
        @Valid @RequestBody taskRequest: TaskRequest,
        authentication: Authentication
    ): ResponseEntity<TaskResponse> {
        logger.info("Create task requested by user={}; title={}", authentication.name, taskRequest.title)
        return try {
            val task = taskService.createTask(authentication.name, taskRequest)
            ResponseEntity.status(HttpStatus.CREATED).body(task)
        } catch (e: Exception) {
            logger.warn("Failed to create task for user={}: {}", authentication.name, e.message)
            logger.debug("CreateTask exception:", e)
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody taskUpdateRequest: TaskUpdateRequest,
        authentication: Authentication
    ): ResponseEntity<TaskResponse> {
        logger.info("Update task requested by user={}; id={}", authentication.name, id)
        return try {
            val task = taskService.updateTask(authentication.name, id, taskUpdateRequest)
            ResponseEntity.ok(task)
        } catch (e: Exception) {
            logger.warn("Failed to update task id={} for user={}: {}", id, authentication.name, e.message)
            logger.debug("UpdateTask exception:", e)
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTask(
        @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<Void> {
        logger.info("Delete task requested by user={}; id={}", authentication.name, id)
        return try {
            taskService.deleteTask(authentication.name, id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            logger.warn("Failed to delete task id={} for user={}: {}", id, authentication.name, e.message)
            logger.debug("DeleteTask exception:", e)
            ResponseEntity.badRequest().build()
        }
    }
}