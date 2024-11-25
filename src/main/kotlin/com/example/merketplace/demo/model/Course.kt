package com.example.merketplace.demo.model



//package com.example.marketplace.model

import jakarta.persistence.*
import java.math.BigDecimal

//import javax.persistence.*

@Entity
@Table(name = "courses")
data class Course(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val price: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: User
)
