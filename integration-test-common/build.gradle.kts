plugins {
    `java-library`
}

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    implementation(project(":doma-slf4j"))
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks {
    compileJava {
        options.incrementalAfterFailure.set(false)
    }
}
