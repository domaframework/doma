description = "doma-core"

dependencies {
    testImplementation(project(":doma-mock"))
    testImplementation(libs.jmh.core)
    testAnnotationProcessor(libs.jmh.generator.annprocess)
}

// Configure JMH annotation processing
tasks.compileTestJava {
    // Override the parent configuration that disables annotation processing
    // Create a new list without "-proc:none"
    options.compilerArgs = options.compilerArgs.filter { it != "-proc:none" }
    // Ensure annotation processor runs
    options.annotationProcessorPath = configurations.testAnnotationProcessor.get()
}

// JMH benchmark configuration
tasks.register<JavaExec>("jmh") {
    group = "benchmark"
    description = "Run JMH benchmarks"
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("org.openjdk.jmh.Main")
    
    // Default JMH arguments
    args("-i", "5", "-wi", "3", "-f", "1", "-t", "1")
    
    // Ensure benchmarks are compiled before running
    dependsOn(tasks.compileTestJava)
}

// Additional JMH tasks for different scenarios
tasks.register<JavaExec>("jmhQuick") {
    group = "benchmark"
    description = "Run JMH benchmarks with quick settings (1 iteration, no warmup)"
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("org.openjdk.jmh.Main")
    args("-i", "1", "-wi", "0", "-f", "1", "-t", "1")
    dependsOn(tasks.compileTestJava)
}

tasks.register<JavaExec>("jmhList") {
    group = "benchmark"
    description = "List all available JMH benchmarks"
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("org.openjdk.jmh.Main")
    args("-l")
    dependsOn(tasks.compileTestJava)
}

tasks.register<JavaExec>("jmhHelp") {
    group = "benchmark"
    description = "Show JMH help"
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("org.openjdk.jmh.Main")
    args("-h")
}
