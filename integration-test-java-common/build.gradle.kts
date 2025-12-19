plugins {
    java
    alias(libs.plugins.themrmilchmann.ecj) apply false
}

val compiler: String by project

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    if (compiler == "ecj") {
        implementation(libs.ecj)
        // See https://github.com/TheMrMilchmann/gradle-ecj/issues/30
        compileOnly(project(":doma-processor"))
    }
}

val commonArgs = listOf("-Adoma.test.integration=true")

// The processors are not automatically detected, so it must be explicitly specified.
val ecjArgs = listOf(
    "-processor",
    listOf(
        "org.seasar.doma.internal.apt.DomaProcessor",
    ).joinToString(","),
)

when (compiler) {
    "javac" -> {
        tasks {
            compileJava {
                options.compilerArgs.addAll(commonArgs)
            }
        }
    }

    "ecj" -> {
        apply(plugin = libs.plugins.themrmilchmann.ecj.get().pluginId)
        tasks {
            compileJava {
                options.compilerArgs.addAll(commonArgs + ecjArgs)
            }
        }
    }

    else -> {
        error("Unknown compiler: $compiler")
    }
}
