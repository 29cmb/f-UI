plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "9.5.1"
    kotlin("plugin.serialization") version "2.4.0"
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
            artifact(tasks.shadowJar)
            groupId = project.group.toString()
            artifactId = "f-ui"
            version = project.version.toString()
        }
    }
}