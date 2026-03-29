plugins {
    id("java")
    kotlin("jvm") version "2.1.21"
}

group = "org.tribot"

repositories {
    mavenCentral()
    maven("https://repo.runelite.net")
}

dependencies {
    compileOnly("net.runelite:client:latest.release")

    // WebSocket + JSON (available at runtime via echo-core)
    compileOnly("com.squareup.okhttp3:okhttp:4.9.2")
    compileOnly("com.google.code.gson:gson:2.8.6")
}

tasks.jar {
    from(sourceSets.main.get().allSource)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
