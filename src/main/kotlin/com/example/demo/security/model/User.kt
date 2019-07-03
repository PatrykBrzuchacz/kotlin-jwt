package com.example.demo.security.model

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.NonNull
import javax.persistence.*

@Entity
data class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        @NonNull
        var username: String,
        @JsonIgnore
        var password: String?,
        @JsonIgnore
        @NonNull
        @ManyToOne
        @JoinColumn(name ="id_role")
        var role: Role)
