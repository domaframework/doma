import org.jetbrains.kotlin.platform.jvm.JvmPlatforms

plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    kapt(project(":doma-processor"))
    implementation(project(":doma-kotlin"))
    testImplementation(project(":integration-test-common"))
}
