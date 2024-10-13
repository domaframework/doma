plugins {
    alias(libs.plugins.kotlin.jvm)
}

description = "doma-kotlin"

dependencies {
    api(project(":doma-core"))
    testImplementation(project(":doma-mock"))
}

tasks.javadoc {
    enabled = false
}
