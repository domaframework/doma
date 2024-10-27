pluginManagement {
    repositories {
        gradlePluginPortal()
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
include("integration-test-kotlin")
