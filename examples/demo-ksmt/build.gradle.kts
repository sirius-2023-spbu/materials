import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven(url = uri("https://jitpack.io"))
}

dependencies {
    testImplementation(kotlin("test"))

    // ksmt core
    implementation("com.github.UnitTestBot.ksmt:ksmt-core:0.3.1")

    // ksmt z3 solver
    implementation("com.github.UnitTestBot.ksmt:ksmt-z3:0.3.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}