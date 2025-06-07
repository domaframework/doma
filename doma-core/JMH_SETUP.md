# JMH Configuration for doma-core

This document explains how JMH (Java Microbenchmark Harness) is configured in the doma-core module.

## Configuration Details

### Dependencies

The JMH dependencies are defined in `gradle/libs.versions.toml`:
- `jmh` version 1.37 (shared version reference)
- `jmh-core` uses `version.ref = "jmh"`
- `jmh-generator-annprocess` uses `version.ref = "jmh"`

These are added to the doma-core module in `build.gradle.kts`:
```kotlin
dependencies {
    testImplementation(libs.jmh.core)
    testAnnotationProcessor(libs.jmh.generator.annprocess)
}
```

### Annotation Processing

The key configuration that enables JMH annotation processing is:
```kotlin
tasks.compileTestJava {
    // Override the parent configuration that disables annotation processing
    options.compilerArgs = options.compilerArgs.filter { it != "-proc:none" }
    // Ensure annotation processor runs
    options.annotationProcessorPath = configurations.testAnnotationProcessor.get()
}
```

This is necessary because the parent build configuration disables annotation processing for test compilation by default.

### Generated Files

When benchmarks are compiled, JMH generates:
1. Benchmark classes in `build/classes/java/test/*/jmh_generated/`
2. The `BenchmarkList` file at `build/classes/java/test/META-INF/BenchmarkList`

## Available Gradle Tasks

### `./gradlew :doma-core:jmh`
Runs all JMH benchmarks with default settings:
- 5 measurement iterations (`-i 5`)
- 3 warmup iterations (`-wi 3`)
- 1 fork (`-f 1`)
- 1 thread (`-t 1`)

Note: Individual benchmark classes may override these settings with their own `@Fork`, `@Warmup`, and `@Measurement` annotations.

### `./gradlew :doma-core:jmhQuick`
Runs benchmarks with minimal settings for quick testing:
- 1 measurement iteration (`-i 1`)
- No warmup iterations (`-wi 0`)
- 1 fork (`-f 1`)
- 1 thread (`-t 1`)
- Useful for verifying benchmark setup

### `./gradlew :doma-core:jmhList`
Lists all available benchmarks without running them.

### `./gradlew :doma-core:jmhHelp`
Shows JMH command-line help.

## Running Specific Benchmarks

To run specific benchmarks, you can pass additional arguments:
```bash
./gradlew :doma-core:jmh --args="SqlTokenUtilBenchmark.optimized*"
```

## Writing Benchmarks

1. Create benchmark classes in the test source set
2. Annotate with JMH annotations like `@Benchmark`, `@State`, `@Setup`
3. Follow JMH best practices for microbenchmarking

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

If the BenchmarkList file is not generated:
1. Ensure annotation processing is not disabled
2. Check that the JMH annotation processor is on the classpath
3. Clean and rebuild: `./gradlew :doma-core:clean :doma-core:compileTestJava`