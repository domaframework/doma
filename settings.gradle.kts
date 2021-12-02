pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.diffplug.eclipse.apt") version "3.33.3"
        id("com.diffplug.spotless") version "6.0.1"
        id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
        id("net.researchgate.release") version "2.8.1"
        id("org.seasar.doma.compile") version "1.1.0"
        kotlin("jvm") version "1.6.0"
        kotlin("kapt") version "1.6.0"
    }
}

rootProject.name = "doma"

include("doma-core")
include("doma-kotlin")
include("doma-mock")
include("doma-processor")
include("doma-slf4j")

include("integration-test-common")
include("integration-test-java")
include("integration-test-java-latest")
include("integration-test-kotlin")
