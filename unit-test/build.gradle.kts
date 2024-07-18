plugins {
    `java-library`
}

dependencies {
    implementation(project(":doma-core"))
    testImplementation(project(":doma-mock"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
