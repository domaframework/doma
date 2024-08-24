plugins {
    `java-library`
}

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    implementation(project(":doma-slf4j"))
    implementation("ch.qos.logback:logback-classic:1.5.7")
    implementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.incrementalAfterFailure.set(false)
    }
}
