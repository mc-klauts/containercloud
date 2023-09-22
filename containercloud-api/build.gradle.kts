plugins {
    id("java")
}

group = "de.dasshorty"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    implementation("com.google.code.gson:gson:2.10.1")
}