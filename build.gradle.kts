plugins {
    base
    kotlin("jvm") version "1.3.72" apply false
    kotlin("kapt") version "1.3.72" apply false
    id("com.diffplug.eclipse.apt") version "3.22.0" apply false
    id("com.diffplug.gradle.spotless") version "3.27.2"
    id("de.marcphilipp.nexus-publish") version "0.4.0" apply false
    id("net.researchgate.release") version "2.8.1"
}

val encoding: String by project
val isSnapshot = version.toString().endsWith("SNAPSHOT")
val releaseVersion = properties["release.releaseVersion"].toString()
val newVersion = properties["release.newVersion"].toString()
val secretKeyRingFile: String? =
        findProperty("signing.secretKeyRingFile")?.toString()?.let {
            if (it.isEmpty()) null
            else file(it).absolutePath
        }

fun replaceVersionInArtifact(ver: String) {
    ant.withGroovyBuilder {
        "replaceregexp"("match" to """(private static final String VERSION = ")[^"]*(")""",
                "replace" to "\\1${ver}\\2",
                "encoding" to encoding,
                "flags" to "g") {
            "fileset"("dir" to ".") {
                "include"("name" to "**/Artifact.java")
            }
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "com.diffplug.eclipse.apt")
    apply(plugin = "com.diffplug.gradle.spotless")
    apply(plugin = "de.marcphilipp.nexus-publish")

    extra["signing.secretKeyRingFile"] = secretKeyRingFile

    val replaceVersionInJava by tasks.registering {
        doLast {
            replaceVersionInArtifact(version.toString())
        }
    }

    val compileJava by tasks.existing(JavaCompile::class) {
        dependsOn(replaceVersionInJava)
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

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.6.2")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            googleJavaFormat("1.7")
        }
        kotlin {
            ktlint("0.36.0")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    configure<org.gradle.plugins.ide.eclipse.model.EclipseModel> {
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
}

configure(subprojects.filter { it.name in listOf("doma-core", "doma-processor") }) {
    val javadoc by tasks.existing(Javadoc::class) {
        options.encoding = encoding
        (options as StandardJavadocDocletOptions).apply {
            charSet = encoding
            docEncoding = encoding
            links("https://docs.oracle.com/javase/8/docs/api/")
            use()
            exclude("**/internal/**")
        }
    }

    val jar by tasks.existing(Jar::class) {
        manifest {
            attributes(mapOf("Implementation-Title" to project.name, "Implementation-Version" to archiveVersion))
        }
    }

    val build by tasks.existing {
        val publishToMavenLocal by tasks.existing
        dependsOn(publishToMavenLocal)
    }

    configure<JavaPluginExtension> {
        withJavadocJar()
        withSourcesJar()
    }

    configure<de.marcphilipp.gradle.nexus.NexusPublishExtension> {
        repositories {
            sonatype()
        }
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                pom {
                    val projectUrl: String by project
                    name.set(project.name)
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
        }
    }

    configure<SigningExtension> {
        val publishing = convention.findByType(PublishingExtension::class)!!
        sign(publishing.publications)
        isRequired = !isSnapshot
    }
}

rootProject.apply {
    apply(from = "release.gradle")

    fun replaceVersionInDocs(ver: String) {
        ant.withGroovyBuilder {
            "replaceregexp"("match" to """("org.seasar.doma:doma-(core|processor)?:)[^"]*(")""",
                    "replace" to "\\1${ver}\\3",
                    "encoding" to encoding,
                    "flags" to "g") {
                "fileset"("dir" to ".") {
                    "include"("name" to "README.md")
                    "include"("name" to "docs/**/*.rst")
                }
            }
        }
    }

    val replaceVersion by tasks.registering {
        doLast {
            replaceVersionInArtifact(releaseVersion)
            replaceVersionInDocs(releaseVersion)
        }
    }

    val beforeReleaseBuild by tasks.existing {
        dependsOn(replaceVersion)
     }

    val updateVersion by tasks.existing {
        doLast {
            replaceVersionInArtifact(newVersion)
        }
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        format("misc") {
            target("**/*.gradle", "**/*.gradle.kts", "**/*.gitignore")
            targetExclude("**/bin/**", "**/build/**")
            indentWithSpaces()
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("documentation") {
            target("docs/**/*.rst", "**/*.md")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
