package com.tyganeutronics.tasks.demo.service

import com.tyganeutronics.tasks.demo.dto.TaskRequest
import com.tyganeutronics.tasks.demo.dto.TaskResponse
import com.tyganeutronics.tasks.demo.dto.TaskUpdateRequest
import com.tyganeutronics.tasks.demo.entity.Task
import com.tyganeutronics.tasks.demo.entity.User
import com.tyganeutronics.tasks.demo.repository.TaskRepository
import com.tyganeutronics.tasks.demo.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import org.slf4j.LoggerFactory

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {

    companion object {
        private val logger = LoggerFactory.getLogger(TaskService::class.java)
    }

    fun getAllTasks(username: String): List<TaskResponse> {
        logger.debug("Fetching all tasks for user={}", username)
        val user = getUserByUsername(username)
        return taskRepository.findByUser(user).map { it.toResponse() }
    }

    fun createTask(username: String, taskRequest: TaskRequest): TaskResponse {
        logger.debug("Creating task for user={}; title={}", username, taskRequest.title)
        val user = getUserByUsername(username)
        val task = Task(
            title = taskRequest.title,
            description = taskRequest.description,
            completed = taskRequest.completed,
            user = user
        )
        val savedTask = taskRepository.save(task)
        logger.info("Task created for user={}; id={}", username, savedTask.id)
        return savedTask.toResponse()
    }

    fun updateTask(username: String, taskId: Long, taskUpdateRequest: TaskUpdateRequest): TaskResponse {
        logger.debug("Updating task id={} for user={}", taskId, username)
        val user = getUserByUsername(username)
        val task = taskRepository.findByUserAndId(user, taskId)
            ?: throw RuntimeException("Task not found or does not belong to user")

        val updatedTask = task.copy(
            title = taskUpdateRequest.title ?: task.title,
            description = taskUpdateRequest.description ?: task.description,
            completed = taskUpdateRequest.completed ?: task.completed,
            updatedAt = LocalDateTime.now()
        )

        val savedTask = taskRepository.save(updatedTask)
        logger.info("Task updated for user={}; id={}", username, savedTask.id)
        return savedTask.toResponse()
    }

    fun deleteTask(username: String, taskId: Long) {
        logger.debug("Deleting task id={} for user={}", taskId, username)
        val user = getUserByUsername(username)
        val task = taskRepository.findByUserAndId(user, taskId)
            ?: throw RuntimeException("Task not found or does not belong to user")

        taskRepository.delete(task)
        logger.info("Task deleted for user={}; id={}", username, taskId)
    }

    fun getTask(username: String, taskId: Long): TaskResponse {
        logger.debug("Fetching task id={} for user={}", taskId, username)
        val user = getUserByUsername(username)
        val task = taskRepository.findByUserAndId(user, taskId)
            ?: throw RuntimeException("Task not found or does not belong to user")

        return task.toResponse()
    }

    private fun getUserByUsername(username: String): User {
        return userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found: $username") }
    }

    private fun Task.toResponse(): TaskResponse {
        return TaskResponse(
            id = this.id,
            title = this.title,
            description = this.description,
            completed = this.completed,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}