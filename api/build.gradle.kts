plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "9.5.1"
    kotlin("plugin.serialization") version "2.4.0"
    `maven-publish`
}

dependencies {
    implementation(libs.adventure)
    implementation(libs.serialization)
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            artifact(tasks.shadowJar) {
                classifier = ""
            }
        }
    }
}