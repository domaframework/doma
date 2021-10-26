plugins {
    java
}

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    implementation(project(":doma-slf4j"))
    implementation("ch.qos.logback:logback-classic:1.2.6")
    implementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}

tasks {
    val encoding = "UTF-8"

    compileJava {
        options.encoding = encoding
    }

    compileTestJava {
        options.encoding = encoding
    }
}
