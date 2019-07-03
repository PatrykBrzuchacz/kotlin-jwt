package com.example.demo.security.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class UsernameAlreadyExistsException : RuntimeException("USERNAME_EXISTS")
