package com.example.springjte

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
// Mapper scanning lives in config/MyBatisConfig.kt so @WebMvcTest slices stay database-free.
class SpringjteApplication

fun main(args: Array<String>) {
	runApplication<SpringjteApplication>(*args)
}
