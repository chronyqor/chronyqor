import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import java.util.*

plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.chronyqor"
version = "1.0.0"
description = "chronyqor"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    // JWT for Authentication
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // Мониторинг и метрики
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Annotation Processors Order
    compileOnly("org.projectlombok:lombok")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // Тестирование
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    // Используем in-memory H2 для тестов
    systemProperty("spring.profiles.active", "test")
    // Показываем стандартный вывод и ошибки тестов
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

tasks.named<BootJar>("bootJar") {
    layered {
        enabled = true
    }

    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Start-Class" to "org.chronyqor.ChronyqorApplication",
                "Main-Class" to "org.springframework.boot.loader.launch.JarLauncher",
                "Built-By" to System.getProperty("user.name"),
                "Built-Date" to Date().toString(),
                "Built-JDK" to System.getProperty("java.version"),
                "Spring-Boot-Version" to "3.5.5"
            )
        )
    }
}

tasks.named<BootRun>("bootRun") {
    systemProperty("spring.profiles.active", "development")
}

// Конфигурация для разработки
springBoot {
    buildInfo()
}

// Настройка компиляции
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}