plugins {
    java
    alias(libs.plugins.themrmilchmann.ecj) apply false
}

val compiler: String by project

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    testImplementation(project(":integration-test-common"))
    if (compiler == "ecj") {
        implementation(libs.ecj)
        // See https://github.com/TheMrMilchmann/gradle-ecj/issues/30
        compileOnly(project(":doma-processor"))
    }
}

when(compiler) {
    "javac" -> {
        tasks {
            compileJava {
                options.compilerArgs.addAll(
                    listOf(
                        "-Adoma.domain.converters=org.seasar.doma.it.domain.DomainConverterProvider",
                        "-Adoma.stats=true",
                    )
                )
            }
        }
    }
    "ecj" -> {
        apply(plugin = libs.plugins.themrmilchmann.ecj.get().pluginId)
        val processors = listOf(
            "org.seasar.doma.internal.apt.processor.DomainProcessor",
            "org.seasar.doma.internal.apt.processor.DataTypeProcessor",
            "org.seasar.doma.internal.apt.processor.ExternalDomainProcessor",
            "org.seasar.doma.internal.apt.processor.DomainConvertersProcessor",
            "org.seasar.doma.internal.apt.processor.EmbeddableProcessor",
            "org.seasar.doma.internal.apt.processor.EntityProcessor",
            "org.seasar.doma.internal.apt.processor.DaoProcessor",
            "org.seasar.doma.internal.apt.processor.ScopeProcessor",
        )
        tasks {
            compileJava {
                @Suppress("UnstableApiUsage")
                options.forkOptions.jvmArgumentProviders.add(
                    CommandLineArgumentProvider {
                        listOf(
                            // The processors are not automatically detected, so it must be explicitly specified.
                            "-processor",
                            processors.joinToString(","),
                            "-Adoma.domain.converters=org.seasar.doma.it.domain.DomainConverterProvider",
                            "-Adoma.stats=true",
                        )
                    }
                )
            }
        }
    }
    else -> error("Unknown compiler: $compiler")
}
