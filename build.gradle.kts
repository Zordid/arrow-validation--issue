plugins {
    kotlin("jvm") version "1.8.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.arrow-kt:arrow-core:1.2.0-RC")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.3.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}