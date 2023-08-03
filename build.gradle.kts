plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "me.akraml"
version = "1.0-SNAPSHOT"

tasks.compileJava {
    options.encoding = "UTF-8"
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.12")
    implementation("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.4")
}