pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.diffplug.eclipse.apt") version "4.0.1"
        id("com.diffplug.spotless") version "6.25.0"
        id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
        id("net.researchgate.release") version "3.0.2"
        id("org.domaframework.doma.compile") version "2.0.0"
        kotlin("jvm") version "2.0.0"
        kotlin("kapt") version "2.0.0"
    }
}

rootProject.name = "doma"

include("doma-core")
include("doma-kotlin")
include("doma-mock")
include("doma-processor")
include("doma-slf4j")
include("doma-template")

include("integration-test-common")
include("integration-test-java")
include("integration-test-java-additional")
include("integration-test-kotlin")
include("unit-test")
