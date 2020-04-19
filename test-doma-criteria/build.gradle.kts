plugins {
    kotlin("jvm") apply true
    kotlin("kapt") apply true
}

kapt {
    arguments {
        arg("doma.entity.field.prefix", "none")
    }
}

dependencies {
    kapt(project(":doma-processor"))
    implementation(project(":doma-core"))
    implementation(project(":doma-criteria"))
    implementation(kotlin("stdlib-jdk8"))
    testRuntimeOnly("com.h2database:h2:1.4.199")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
