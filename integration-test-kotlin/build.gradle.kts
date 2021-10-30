import org.jetbrains.kotlin.platform.jvm.JvmPlatforms

plugins {
    kotlin("jvm")
    kotlin("kapt")
}

val javaLangVersion: Int = project.properties["testJavaLangVersion"].toString().toInt()

dependencies {
    kapt(project(":doma-processor"))
    implementation(project(":doma-kotlin"))
    testImplementation(project(":integration-test-common"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaLangVersion))
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = if (javaLangVersion <= 8) "1.8" else javaLangVersion.toString()
    }
}
