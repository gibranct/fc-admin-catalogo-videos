import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



buildscript {
    val kotlinVersion = "1.6.21"

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.flywaydb:flyway-mysql:8.5.10")
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    }
}

apply {
    plugin("kotlin-jpa")
}

plugins {
    id("application")
    id("org.springframework.boot") version "2.6.8"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.flywaydb.flyway") version "8.5.10"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
    kotlin("plugin.jpa") version "1.7.0"
}

group = "com.fullcycle.admin.catalogo.infrastructure"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.bootJar {
    archiveFileName.set("application.jar")
    destinationDirectory.set(file("${rootProject.buildDir}/libs"))
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))

    implementation("com.google.cloud:google-cloud-storage:2.20.1")
    implementation("com.google.guava:guava:31.1-jre")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // implementation("javax.inject:javax.inject:1") // faz com que não seja necessário criar classes de configuração pra classes do Domain

    implementation("mysql:mysql-connector-java")

    implementation("org.springdoc:springdoc-openapi-webmvc-core:1.6.13")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.15")

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.flywaydb:flyway-core")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-junit-jupiter:5.2.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

    implementation("io.arrow-kt:arrow-core:1.1.2")

    implementation("com.fasterxml.jackson.module:jackson-module-afterburner")

    implementation("org.testcontainers:testcontainers:1.17.6")
    implementation("org.testcontainers:mysql:1.17.6")
    implementation("org.testcontainers:junit-jupiter:1.17.6")

    testRuntimeOnly("com.h2database:h2")
}

flyway {
    url = System.getenv("FLYWAY_DB") ?: "jdbc:mysql://127.0.0.1:3306/adm_videos?useSSL=true&serverTimezone=UTC&characterEncoding=UTF-8"
    user = System.getenv("FLYWAY_USER") ?: "root"
    password = System.getenv("FLYWAY_PASS") ?: "123456"
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}