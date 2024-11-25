package com.example.merketplace.demo.controller

//package com.example.marketplace.controller

import com.example.merketplace.demo.repository.CourseRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDate

data class StatsResponse(val totalCourses: Int, val totalAmount: BigDecimal)

@RestController
@RequestMapping("/stats")
class StatsController(private val courseRepository: CourseRepository) {

    @GetMapping
    fun getStats(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ResponseEntity<StatsResponse> {
        // For simplicity, we're not implementing date filtering in this example
        val courses = courseRepository.findAll()
        val totalCourses = courses.size
        val totalAmount = courses.sumOf { it.price }

        return ResponseEntity.ok(StatsResponse(totalCourses, totalAmount))
    }



}
