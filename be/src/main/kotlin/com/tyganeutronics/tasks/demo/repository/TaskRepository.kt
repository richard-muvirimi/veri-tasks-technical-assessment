package com.tyganeutronics.tasks.demo.repository

import com.tyganeutronics.tasks.demo.entity.Task
import com.tyganeutronics.tasks.demo.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findByUser(user: User): List<Task>
    fun findByUserAndId(user: User, id: Long): Task?
}