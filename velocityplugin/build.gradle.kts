plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "de.containercloud"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation(project(":containercloud-api"))

    implementation("com.esotericsoftware:kryonet:2.22.0-RC1")
}

tasks.test {
    useJUnitPlatform()
}