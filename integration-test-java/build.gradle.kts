plugins {
    java
}

val javaReleaseVersion: Int = project.properties["javaReleaseVersion"]?.toString()?.toInt() ?: 8
val moduleInfoFile = "$projectDir/src/main/java/module-info.java"

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    testImplementation(project(":integration-test-common"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}

tasks {
    val encoding = "UTF-8"

    val prepareModuleInfo by registering {
        doLast {
            delete(moduleInfoFile)
            if (javaReleaseVersion > 8) {
                file(moduleInfoFile).writeText("""
                open module org.seasar.doma.it.java {
                    requires org.seasar.doma.core;
                }
            """.trimIndent())
            }
        }
    }

    compileJava {
        dependsOn(prepareModuleInfo)
        options.encoding = encoding
        options.release.set(javaReleaseVersion)
        options.compilerArgs.addAll(listOf("-Adoma.domain.converters=org.seasar.doma.it.domain.DomainConverterProvider"))
        doLast {
        }
    }

    compileTestJava {
        options.encoding = encoding
        options.compilerArgs.addAll(listOf("-proc:none"))
    }
}
