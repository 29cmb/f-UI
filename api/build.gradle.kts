plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "9.5.1"
    kotlin("plugin.serialization") version "2.4.0"
    `maven-publish`
}

version = "1.0.0"

dependencies {
    implementation(libs.adventure)
    implementation(libs.serialization)
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["java"])
                groupId = "com.github.29cmb"
                artifactId = "f-ui"
                version = project.version.toString()
            }
        }
    }
}