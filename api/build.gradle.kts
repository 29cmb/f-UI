plugins {
    kotlin("jvm") version  "2.4.20-Beta1"
    id("com.gradleup.shadow") version "9.5.1"
    kotlin("plugin.serialization") version "2.4.0"
}

dependencies {
    implementation(libs.adventure)
    implementation(libs.serialization)
}