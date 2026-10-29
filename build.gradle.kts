import java.nio.file.Paths

plugins {
	kotlin("jvm") version "2.3.21"
	kotlin("plugin.spring") version "2.3.21"
	war
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("gg.jte.gradle") version "3.2.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// --- Spring Boot Core ---
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-flyway")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.flywaydb:flyway-core")
	// --- Persistence (MyBatis) ---
	// 4.1.x is the line built against Spring Boot 4.1 (Spring Framework 7 / Jakarta EE 11).
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:4.1.0")
	// --- Kotlin Support ---
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	// --- Template Engine (JTE) ---
	implementation("gg.jte:jte:3.2.4")
	implementation("gg.jte:jte-spring-boot-starter-4:3.2.4")
	// --- Logging ---
	implementation("org.springframework.boot:spring-boot-starter-logging")
	// --- Database ---
	runtimeOnly("org.xerial:sqlite-jdbc")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat-runtime")
	// --- Testing ---
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
	testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
	testImplementation("org.springframework.boot:spring-boot-starter-jdbc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

jte {
	generate()
	sourceDirectory.set(Paths.get("src/main/resources/templates"))
}

tasks.withType<Test> {
	useJUnitPlatform()
}
