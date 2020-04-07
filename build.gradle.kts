import org.gradle.api.publish.maven.MavenPom

plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
    id("com.diffplug.eclipse.apt") version "3.22.0"
    id("com.diffplug.gradle.spotless") version "3.27.2"
    id("net.researchgate.release") version "2.8.1"
}

apply("release.gradle")

val encoding: String by project

val compileJava by tasks.existing(JavaCompile::class) {
    dependsOn(tasks.named("replaceVersion"))
    options.encoding = encoding
}

val compileTestJava by tasks.existing(JavaCompile::class) {
    options.encoding = encoding
    options.compilerArgs = listOf("-proc:none")
}

val test by tasks.existing(Test::class) {
    maxHeapSize = "1g"
    useJUnitPlatform()
}

val javadoc by tasks.existing(Javadoc::class) {
    options.encoding = encoding
    (options as StandardJavadocDocletOptions).apply {
        charSet = encoding
        docEncoding = encoding
        links("https://docs.oracle.com/javase/jp/8/docs/api/")
        use()
        exclude("**/internal/**")
    }
}

val jar by tasks.existing(Jar::class) {
    manifest {
        attributes(mapOf("Implementation-Title" to "Doma", "Implementation-Version" to archiveVersion))
    }
    exclude("**/apt/**", "META-INF/**")
}

val processorJar by tasks.registering(Jar::class) {
    manifest {
        attributes(mapOf("Implementation-Title" to "Doma Processor", "Implementation-Version" to archiveVersion))
    }
    archiveAppendix.set("processor")
    from(compileJava.get().destinationDirectory)
    from(tasks.processResources.get().destinationDir)
}

val processorSourcesJar by tasks.registering(Jar::class) {
    archiveAppendix.set("processor")
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val build by tasks.existing {
    val publishToMavenLocal by tasks.existing
    dependsOn(publishToMavenLocal)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.0")
}

publishing {
    publications {
        val main = "main"
        val processor = "processor"
        create<MavenPublication>(main) {
            from(components["java"])
            pom(pomDefinition(main))
            repositories(repositoriesDefinition())
        }
        create<MavenPublication>(processor) {
            pom(pomDefinition(processor))
            val jar = processorJar.get()
            val sourcesJar = processorSourcesJar.get()
            artifactId = "${jar.archiveBaseName.get()}-${jar.archiveAppendix.get()}"
            artifact(jar)
            artifact(sourcesJar)
            repositories(repositoriesDefinition())
        }
    }
}

fun MavenPublication.pomDefinition(publicationName: String): MavenPom.() -> Unit {
    return {
        val projectUrl: String by project
        name.set(publicationName)
        description.set("DAO Oriented Database Mapping Framework for Java 8+")
        url.set(projectUrl)
        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("nakamura-to")
                name.set("Toshihiro Nakamura")
                email.set("toshihiro.nakamura@gmail.com")
            }
        }
        scm {
            val githubUrl: String by project
            connection.set("scm:git:${githubUrl}")
            developerConnection.set("scm:git:${githubUrl}")
            url.set(projectUrl)
        }
    }
}

fun MavenPublication.repositoriesDefinition(): RepositoryHandler.() -> Unit {
    return {
        maven {
            val sonatypeSnapshotUrl: String by project
            val sonatypeUrl: String by project
            val sonatypeUsername: String by project
            val sonatypePassword: String by project
            url = uri(if (version.endsWith("SNAPSHOT")) sonatypeSnapshotUrl else sonatypeUrl)
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}

eclipse {
    classpath {
        file {
            whenMerged {
                val classpath = this as org.gradle.plugins.ide.eclipse.model.Classpath
                classpath.entries.removeAll {
                    when (it) {
                        is org.gradle.plugins.ide.eclipse.model.Output -> it.path == ".apt_generated"
                        else -> false
                    }
                }
            }
            withXml {
                val node = asNode()
                node.appendNode("classpathentry", mapOf("kind" to "src", "output" to "bin/main", "path" to ".apt_generated"))
            }
        }
    }
    jdt {
        javaRuntimeName = "JavaSE-1.8"
    }
}
