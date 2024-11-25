package com.example.merketplace.demo.service



import com.example.merketplace.demo.model.User
import com.example.merketplace.demo.model.UserRole
import com.example.merketplace.demo.repository.UserRepository
//import com.marketplace.api.model.User
//import com.marketplace.api.model.UserRole
//import com.marketplace.api.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun createUser(email: String, password: String, role: UserRole): User {
        val encodedPassword = passwordEncoder.encode(password)
        val user = User(email = email, password = encodedPassword, role = role)
        return userRepository.save(user)
    }

    fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun validateUser(email: String, password: String): Boolean {
        val user = findUserByEmail(email) ?: return false
        return passwordEncoder.matches(password, user.password)
    }
}
