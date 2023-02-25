plugins {
    java
}

val moduleInfoFile = "$projectDir/src/main/java/module-info.java"
val useModule: Boolean = project.properties["testUseModule"].toString().toBoolean()
val javaLangVersion: Int = project.properties["testJavaLangVersion"].toString().toInt()

delete(moduleInfoFile)
if (useModule) {
    file(moduleInfoFile).writeText("""
                open module org.seasar.doma.it.java {
                    requires org.seasar.doma.core;
                }
            """.trimIndent()
    )
}

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    testImplementation(project(":integration-test-common"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaLangVersion))
}

tasks {
    compileJava {
        options.compilerArgs.addAll(listOf("-Adoma.domain.converters=org.seasar.doma.it.domain.DomainConverterProvider"))
        options.incrementalAfterFailure.set(false)
    }

    compileTestJava {
        options.compilerArgs.addAll(listOf("-proc:none"))
    }
}
