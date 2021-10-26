plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    kapt(project(":doma-processor"))
    implementation(project(":doma-kotlin"))
    testImplementation(project(":integration-test-common"))
}

spotless {
    kotlin {
        ktlint("0.38.1")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
