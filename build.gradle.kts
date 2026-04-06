plugins {
    id("java-library")
    kotlin("jvm") version "2.1.21"
    `maven-publish`
}

group = "org.tribot"
version = System.getenv("VERSION") ?: "0.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.runelite.net")
}

dependencies {
    api("net.runelite:client:latest.release")

    // WebSocket + JSON — transitive for standalone consumers, already on classpath in echo
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.google.code.gson:gson:2.8.6")
}

tasks.jar {
    from(sourceSets.main.get().allSource)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
