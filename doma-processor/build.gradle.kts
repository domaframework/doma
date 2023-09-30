plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

description = "doma-processor"

dependencies {
    implementation(project(":doma-core"))
    testImplementation(project(":doma-mock"))
}
