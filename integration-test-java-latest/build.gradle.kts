plugins {
    java
}

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    testImplementation(project(":integration-test-common"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

//spotless {
//    java {
//        googleJavaFormat("1.9")
//    }
//}

tasks {
    val encoding = "UTF-8"

    withType<JavaCompile> {
        options.encoding = encoding
    }

    withType<Test> {}
}
