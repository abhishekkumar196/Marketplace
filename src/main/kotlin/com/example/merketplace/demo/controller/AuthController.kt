package com.example.merketplace.demo.controller

import com.example.merketplace.demo.model.User
import com.example.merketplace.demo.model.UserRole
import com.example.merketplace.demo.repository.UserRepository
import com.example.merketplace.demo.security.CustomUserDetailsService
import com.example.merketplace.demo.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class SignupRequest(val email: String, val password: String, val role: UserRole)
data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val token: String)

@RestController
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/signup")
    fun signup(@RequestBody signupRequest: SignupRequest): ResponseEntity<AuthResponse> {
        if (userRepository.findByEmail(signupRequest.email) != null) {
            return ResponseEntity.badRequest().body(AuthResponse("Email is already in use"))
        }

        val newUser = User(
            email = signupRequest.email,
            password = passwordEncoder.encode(signupRequest.password),
            role = signupRequest.role
        )
        userRepository.save(newUser)

        val userDetails = userDetailsService.loadUserByUsername(newUser.email)
        val jwt = jwtUtil.generateToken(userDetails)
        return ResponseEntity.ok(AuthResponse(jwt))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
            )
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(AuthResponse("Invalid email or password"))
        }

        val userDetails = userDetailsService.loadUserByUsername(loginRequest.email)
        val jwt = jwtUtil.generateToken(userDetails)
        return ResponseEntity.ok(AuthResponse(jwt))
    }
}
