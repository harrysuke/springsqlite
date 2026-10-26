package com.example.springjte.config

import org.apache.ibatis.annotations.Mapper
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration

/**
 * Registers every `@Mapper` interface in the MyBatis package as a bean backed by
 * the auto-configured `SqlSessionFactory`.
 *
 * This lives here rather than on [com.example.springjte.SpringjteApplication]
 * because `@WebMvcTest` slices load the application class and would otherwise
 * try to create the mappers without a DataSource, failing the context with
 * *"Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required"*. A plain
 * `@Configuration` is component-scanned by `@SpringBootTest` but ignored by the
 * MVC slice.
 */
@Configuration(proxyBeanMethods = false)
@MapperScan(basePackages = ["com.example.springjte.repository.mybatis"], annotationClass = Mapper::class)
class MyBatisConfig
