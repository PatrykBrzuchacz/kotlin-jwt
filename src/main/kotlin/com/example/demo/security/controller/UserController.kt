package com.example.demo.security.controller

import com.example.demo.security.model.User
import com.example.demo.security.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class UserController(private val userRepository: UserRepository) {

    @GetMapping("/users")
    fun getAllUsers(): List<User> = userRepository.findAll();

    @PostMapping("/users")
    fun addUser(@RequestBody user: User) = userRepository.save(user);

    @PutMapping("/users/{id}")
    fun updateUser(@PathVariable("id") id: Long, @RequestBody user: User): ResponseEntity<User> {
        return userRepository.findById(id).map { existingUser ->
            val newUser: User = existingUser.copy(firstName = user.firstName, lastName = user.lastName, age = user.age)
            ResponseEntity.ok().body(userRepository.save(newUser))}.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
        }
//        return ResponseEntity.ok().body(userRepository.save(user))}.orElse(ResponseEntity.notFound().build())

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable("id") id: Long): ResponseEntity<User> {
        return userRepository.findById(id).map {user ->
            ResponseEntity.ok(user)
        }.orElse(ResponseEntity.notFound().build())
        //return ResponseEntity.ok().body(userRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable("id") id: Long): ResponseEntity<Void>{
        return userRepository.findById(id).map{ user ->
            userRepository.delete(user)
            ResponseEntity<Void>(HttpStatus.OK)}.orElse(ResponseEntity.notFound().build())
        }
    }
//    ResponseEntity<Void>(HttpStatus.OK)}.orElse(ResponseEntity(HttpStatus.NOT_FOUND))

