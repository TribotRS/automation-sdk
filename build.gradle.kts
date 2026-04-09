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
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// ─── Release task ───
//
// Triggers the GitHub release workflow on TribotRS/automation-sdk. The workflow creates a
// git tag `v<releaseVersion>` and a GitHub release, which JitPack picks up automatically
// to publish under com.github.TribotRS:automation-sdk:v<version>.
//
// Prerequisites:
//   - The `gh` CLI must be installed and authenticated against the TribotRS org.
//   - The monorepo's automation-sdk subtree must already be pushed to the public repo's
//     main branch (this task does NOT push code).
//
// Usage:
//   ./gradlew release -PreleaseVersion=1.2.3
tasks.register("release") {
    group = "tribot"
    description = "Triggers a GitHub release workflow for automation-sdk on the public repo"

    doLast {
        val releaseVersion = (project.findProperty("releaseVersion") as? String)
            ?: error("Must specify release version: ./gradlew release -PreleaseVersion=1.2.3")

        require(releaseVersion.matches(Regex("""^\d+\.\d+\.\d+(-[a-zA-Z0-9.]+)?$"""))) {
            "releaseVersion must look like 1.2.3 or 1.2.3-rc1, got: $releaseVersion"
        }

        val cmd = listOf(
            "gh", "workflow", "run", "release.yml",
            "--repo", "TribotRS/automation-sdk",
            "--ref", "main",
            "-f", "version=$releaseVersion",
        )

        logger.lifecycle("Running: ${cmd.joinToString(" ")}")

        val process = ProcessBuilder(cmd)
            .directory(rootDir)
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText()
        val exit = process.waitFor()
        if (output.isNotBlank()) logger.lifecycle(output.trim())
        if (exit != 0) {
            error("gh workflow run failed with exit $exit — is the gh CLI authenticated and is the main branch pushed?")
        }

        logger.lifecycle("Triggered automation-sdk release for v$releaseVersion. Check progress at:")
        logger.lifecycle("  https://github.com/TribotRS/automation-sdk/actions")
    }
}
