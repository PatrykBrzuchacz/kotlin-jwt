package com.example.demo.security.model

import java.util.ArrayList
import javax.persistence.*

data class Role(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        var name: String,
        var description: String,
        @OneToMany(mappedBy = "roles", cascade = [CascadeType.ALL])
        private var users: ArrayList<User>
)