pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.diffplug.eclipse.apt") version "3.42.2"
        id("com.diffplug.spotless") version "6.21.0"
        id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
        id("net.researchgate.release") version "3.0.2"
        id("org.domaframework.doma.compile") version "2.0.0"
        kotlin("jvm") version "1.9.10"
        kotlin("kapt") version "1.9.10"
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
