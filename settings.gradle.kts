pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.diffplug.eclipse.apt") version "3.33.1"
        id("com.diffplug.spotless") version "5.17.1"
        id("de.marcphilipp.nexus-publish") version "0.4.0"
        id("io.codearte.nexus-staging") version "0.30.0"
        id("net.researchgate.release") version "2.8.1"
        id("org.seasar.doma.compile") version "1.1.0"
        kotlin("jvm") version "1.5.31"
        kotlin("kapt") version "1.6.0-RC"
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
