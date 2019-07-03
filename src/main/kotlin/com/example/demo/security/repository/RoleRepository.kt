package com.example.demo.security.repository

import com.example.demo.security.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role,Long>