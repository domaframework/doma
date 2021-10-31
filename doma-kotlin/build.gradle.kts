plugins {
    kotlin("jvm") apply true
}

description = "doma-kotlin"

dependencies {
    api(project(":doma-core"))
    testImplementation(project(":doma-mock"))
}
