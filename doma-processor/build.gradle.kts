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
        systemProperty("classpath", classpath.asPath)
    }
}
