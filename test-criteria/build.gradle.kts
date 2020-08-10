plugins {
    kotlin("jvm") apply true
    kotlin("kapt") apply true
}

dependencies {
    kapt(project(":doma-processor"))
    implementation(project(":doma-core"))
    implementation(kotlin("stdlib-jdk8"))
    testRuntimeOnly("com.h2database:h2:1.4.200")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
