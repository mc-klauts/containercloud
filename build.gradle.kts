plugins {
    id("java")
}

group = "de.dasshorty"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // docker api's
    implementation("com.github.docker-java:docker-java-core:3.3.3")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:3.3.3")

    // project code
    implementation(project(":shortcloud-api"))

    // Logger
    implementation("org.slf4j:slf4j-simple:2.0.9")

    // 3rd party api's
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    // mongo
    implementation("org.mongodb:mongodb-driver-sync:4.10.2")
}