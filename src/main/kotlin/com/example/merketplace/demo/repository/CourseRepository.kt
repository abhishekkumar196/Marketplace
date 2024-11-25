package com.example.merketplace.demo.repository


import com.example.merketplace.demo.model.Course
import com.example.merketplace.demo.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, Long> {
    fun findByCreatorId(creatorId: Long): List<Course>
    fun findByCreator(creator: User): List<Course>
}
