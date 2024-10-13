description = "doma-slf4j"

dependencies {
    api(libs.slf4j.api)
    implementation(project(":doma-core"))
    testImplementation(libs.logback.classic)
}
