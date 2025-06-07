# JMH Configuration for doma-core

This document explains how JMH (Java Microbenchmark Harness) is configured in the doma-core module using the JMH Gradle plugin.

## Configuration Details

### Plugin Configuration

The JMH plugin is configured in `gradle/libs.versions.toml`:
```toml
[plugins]
jmh = { id = "me.champeau.jmh", version = "0.7.3" }
```

And applied in `doma-core/build.gradle.kts`:
```kotlin
plugins {
    alias(libs.plugins.jmh)
}
```

### JMH Settings

The benchmark configuration supports multiple profiles for different use cases:

#### Development Profile (Default - Fast)
```bash
./gradlew :doma-core:jmh                        # Uses dev profile by default
./gradlew :doma-core:jmh -Pjmh.profile=dev     # Explicit dev profile
```
- **Time**: ~30 seconds for all benchmarks
- **Iterations**: 1 measurement, 1 warmup
- **Forks**: 1
- **Duration**: 1 second per iteration
- **Use case**: Quick feedback during development

#### CI Profile (Balanced)
```bash
./gradlew :doma-core:jmh -Pjmh.profile=ci
```
- **Time**: ~3-5 minutes for all benchmarks
- **Iterations**: 3 measurement, 2 warmup
- **Forks**: 1
- **Duration**: 3 seconds per iteration
- **Use case**: Continuous integration, pull request validation

#### Production Profile (Accurate)
```bash
./gradlew :doma-core:jmh -Pjmh.profile=production
```
- **Time**: ~20-30 minutes for all benchmarks
- **Iterations**: 10 measurement, 5 warmup
- **Forks**: 3
- **Duration**: 10 seconds per iteration
- **Use case**: Release benchmarks, performance regression analysis

The JMH task is configured to bypass Gradle's caching mechanism to ensure benchmarks always run and provide fresh performance measurements.

### Dependency Management

The JMH plugin automatically handles all JMH dependencies including:
- `org.openjdk.jmh:jmh-core`
- `org.openjdk.jmh:jmh-generator-annprocess`
- Code generation and annotation processing

No manual dependency configuration is required.

### Benchmark Source Location

Benchmark classes are located in:
- `src/jmh/java/org/seasar/doma/internal/util/SqlTokenUtilBenchmark.java`
- `src/jmh/java/org/seasar/doma/internal/jdbc/sql/SqlTokenizerBenchmark.java`

## Available Gradle Tasks

### `./gradlew :doma-core:jmh`
Runs all JMH benchmarks with the configured settings:
- 5 measurement iterations (10 seconds each by default)
- 3 warmup iterations (10 seconds each by default)
- 2 forks
- Results in nanoseconds
- JVM args: `-Xms2G -Xmx2G`

### `./gradlew :doma-core:jmhJar`
Creates a self-contained JAR file with all benchmarks.

### `./gradlew :doma-core:jmhCompileGeneratedClasses`
Compiles JMH generated classes.

### `./gradlew :doma-core:jmhRunBytecodeGenerator`
Runs the JMH bytecode generator.

## Running Specific Benchmarks

### Method 1: Configure in build.gradle.kts
You can configure the JMH plugin to run specific benchmarks by modifying the build file:
```kotlin
jmh {
    includes.set(listOf(".*SqlTokenUtil.*"))  // Run only SqlTokenUtil benchmarks
    excludes.set(listOf(".*classic.*"))       // Exclude classic benchmarks
}
```

### Method 2: Dynamic filtering with Gradle properties
The current build script already supports dynamic filtering:
```kotlin
val jmhIncludes: String? = findProperty("jmh.includes") as String?
jmh {
    iterations.set(5)
    warmupIterations.set(3)
    fork.set(2)
    timeUnit.set("ns")
    resultFormat.set("TEXT")
    jvmArgs.set(listOf("-Xms2G", "-Xmx2G"))

    if (jmhIncludes != null) {
        includes.set(listOf(jmhIncludes))
    }
}
```

Then run with:
```bash
./gradlew :doma-core:jmh -Pjmh.includes=".*SqlTokenUtil.*"
```

## Writing Benchmarks

1. Create benchmark classes in `src/jmh/java`
2. Annotate with JMH annotations like `@Benchmark`, `@State`, `@Setup`
3. Configuration is handled by the Gradle plugin, not annotations
4. Follow JMH best practices for microbenchmarking

Example structure:
```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class MyBenchmark {
    @Benchmark
    public void testMethod() {
        // benchmark code
    }
}
```

## Troubleshooting

If benchmarks don't run:
1. Ensure benchmark classes are in `src/jmh/java`
2. Check that the JMH plugin is properly applied
3. Clean and rebuild: `./gradlew :doma-core:clean :doma-core:jmh`
