package com.example.demo.security.service

import com.example.demo.security.model.User
import com.example.demo.security.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.HashSet

@Service
class UserService(var userRepository: UserRepository) : UserDetailsService{

    override fun loadUserByUsername(username: String): UserDetails {
        var user: User = userRepository.findByUsername(username)
        return org.springframework.security.core.userdetails.User(user.username, user.password, getAuthority
        (user))
    }

    fun getAuthority(user: User): Set<SimpleGrantedAuthority>{
        val authorities = HashSet<SimpleGrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(user.role.name))
        return authorities
    }

    fun createUser(user: User): User {
        
    }
}