package com.example.demo.config

import com.example.demo.security.model.User
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.stereotype.Component

@Component
class RestConfiguration : RepositoryRestConfigurer {

    override fun configureRepositoryRestConfiguration(config : RepositoryRestConfiguration) : Unit {
        config.setBasePath("/api")
        config.exposeIdsFor(User::class.java)
    }
}