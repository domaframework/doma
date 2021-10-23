plugins {
    kotlin("jvm") apply true
}

description = "doma-kotlin"

dependencies {
    api(project(":doma-core"))
    testImplementation(project(":doma-mock"))
}

tasks {
    val jvmTarget = "1.8"

    compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }
    javadoc {
        enabled = false
    }
}
