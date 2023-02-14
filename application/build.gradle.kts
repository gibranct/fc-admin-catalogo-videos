import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
}

group = "com.fullcycle.admin.catalogo.application"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))

    testImplementation("com.github.javafaker:javafaker:1.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.mockito:mockito-junit-jupiter:4.6.1")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    implementation(kotlin("stdlib-jdk8"))
    implementation("io.arrow-kt:arrow-core:1.0.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}