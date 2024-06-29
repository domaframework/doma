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
    compileJava {
        options.incrementalAfterFailure.set(false)
    }

    compileTestJava {
        options.compilerArgs.addAll(listOf("-proc:none"))
    }
}
