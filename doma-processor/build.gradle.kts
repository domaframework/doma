plugins {
    alias(libs.plugins.shadow)
}

description = "doma-processor"

dependencies {
    implementation(project(":doma-core"))
    testImplementation(project(":doma-mock"))
    testImplementation(libs.ecj)
}

tasks {
    test {
        val compiler: String by project
        systemProperty("compiler", compiler)
        systemProperty("junit.jupiter.execution.parallel.enabled", "true")
        systemProperty("junit.jupiter.execution.parallel.mode.default", "concurrent")
        systemProperty("user.timezone", "Asia/Tokyo")
    }
}
