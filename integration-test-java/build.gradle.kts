plugins {
    java
}

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    testImplementation(project(":integration-test-common"))
}

tasks {
    compileJava {
        options.compilerArgs.addAll(listOf("-Adoma.domain.converters=org.seasar.doma.it.domain.DomainConverterProvider"))
    }
}
