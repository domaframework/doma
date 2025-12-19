plugins {
    alias(libs.plugins.jmh)
}

description = "doma-core"

dependencies {
    testImplementation(project(":doma-mock"))
}

// JMH plugin configuration
val jmhIncludes: String? = findProperty("jmh.includes") as String?
val jmhProfile: String = findProperty("jmh.profile") as String? ?: "dev"

jmh {
    jmhVersion.set(libs.jmh.core.get().version) // Use JMH version from libs.versions.toml
    timeUnit.set("ns") // Time unit for results
    resultFormat.set("TEXT") // Result format
    jvmArgs.set(listOf("-Xms2G", "-Xmx2G")) // JVM arguments

    // Profile-based configuration
    when (jmhProfile) {
        "dev", "development" -> {
            // Fast development profile
            iterations.set(1) // Quick measurement
            warmupIterations.set(1) // Minimal warmup
            fork.set(1) // Single fork
            timeOnIteration.set("1s") // 1 second per iteration
        }

        "ci" -> {
            // CI profile - balanced speed vs accuracy
            iterations.set(3) // Moderate measurement
            warmupIterations.set(2) // Light warmup
            fork.set(1) // Single fork for speed
            timeOnIteration.set("3s") // 3 seconds per iteration
        }

        "production", "prod" -> {
            // Production profile - high accuracy
            iterations.set(10) // Thorough measurement
            warmupIterations.set(5) // Proper warmup
            fork.set(3) // Multiple forks for stability
            timeOnIteration.set("10s") // 10 seconds per iteration
        }

        else -> {
            // Default profile (moderate)
            iterations.set(5)
            warmupIterations.set(3)
            fork.set(2)
            timeOnIteration.set("5s")
        }
    }

    // Dynamic filtering support
    if (jmhIncludes != null) {
        includes.set(listOf(jmhIncludes))
    }
}

// Configure JMH task to disable caching and always run
tasks.named("jmh") {
    outputs.upToDateWhen { false }
    doNotTrackState("Benchmarks should always run to get fresh results")
}
