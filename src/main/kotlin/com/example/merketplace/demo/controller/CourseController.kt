package com.example.merketplace.demo.controller


import com.example.merketplace.demo.model.Course
import com.example.merketplace.demo.model.UserRole
import com.example.merketplace.demo.repository.CourseRepository
import com.example.merketplace.demo.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

data class CreateCourseRequest(val title: String, val description: String, val price: BigDecimal)

@RestController
@RequestMapping("/course")
class CourseController(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository
) {

    @PostMapping
    fun createCourse(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody request: CreateCourseRequest
    ): ResponseEntity<Course> {
        val user = userRepository.findByEmail(userDetails.username)
            ?: return ResponseEntity.badRequest().build()

        if (user.role != UserRole.CREATOR) {
            return ResponseEntity.status(403).build()
        }

        val course = Course(
            title = request.title,
            description = request.description,
            price = request.price,
            creator = user
        )

        val savedCourse = courseRepository.save(course)
        return ResponseEntity.ok(savedCourse)
    }

    @GetMapping
    fun getCourses(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<List<Course>> {
        val user = userRepository.findByEmail(userDetails.username)
            ?: return ResponseEntity.badRequest().build()

        val courses = when (user.role) {
            UserRole.CREATOR -> courseRepository.findByCreatorId(user.id)
            UserRole.CUSTOMER -> courseRepository.findAll()
            else -> return ResponseEntity.status(403).build()
        }

        return ResponseEntity.ok(courses)
    }
}
