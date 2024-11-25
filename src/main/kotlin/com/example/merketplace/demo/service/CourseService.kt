package com.example.merketplace.demo.service


import com.example.merketplace.demo.model.Course
import com.example.merketplace.demo.model.User
import com.example.merketplace.demo.repository.CourseRepository
import org.springframework.stereotype.Service

@Service
class CourseService(private val courseRepository: CourseRepository) {

    fun createCourse(title: String, description: String, price: Double, creator: User): Course {
        val course = Course(title = title, description = description, price = price, creator = creator)
        return courseRepository.save(course)
    }

    fun getCoursesByCreator(creator: User): List<Course> {
        return courseRepository.findByCreator(creator)
    }

    fun getAllCourses(): List<Course> {
        return courseRepository.findAll()
    }

    fun searchCourses(query: String): List<Course> {
        return courseRepository.findAll().filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
    }
}
