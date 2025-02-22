plugins {
    `java-library`
}

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    implementation(project(":doma-slf4j"))
    implementation(libs.logback.classic)
    implementation(platform(libs.junit.bom))
    implementation(libs.junit.jupiter.api)
}
