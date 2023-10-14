plugins {
    id("java")
}

group = "de.dasshorty"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
}