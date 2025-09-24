package com.tyganeutronics.tasks.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HelloController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World!"
    }

    @GetMapping("/hello/{name}")
    fun helloWithName(name: String): String {
        return "Hello, $name!"
    }
}