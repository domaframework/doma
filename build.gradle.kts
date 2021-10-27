plugins {
    base
    id("com.diffplug.eclipse.apt") version "3.33.1" apply false
    id("com.diffplug.spotless") version "5.17.1"
    id("de.marcphilipp.nexus-publish") version "0.4.0" apply false
    id("io.codearte.nexus-staging") version "0.30.0"
    id("net.researchgate.release") version "2.8.1"
    kotlin("jvm") version "1.5.31" apply false
}

val Project.javaModuleName: String
    get() = "org.seasar." + this.name.replace('-', '.')

val modularProjects: List<Project> = listOf(
    findProject(":doma-core")!!,
    findProject(":doma-kotlin")!!,
    findProject(":doma-mock")!!,
    findProject(":doma-processor")!!,
    findProject(":doma-slf4j")!!,
)

val encoding: String by project
val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")

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
    apply(plugin = "com.diffplug.spotless")

    tasks {
        named("build") {
            dependsOn(spotlessApply)
        }
    }

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "com.diffplug.eclipse.apt")
    apply(plugin = "de.marcphilipp.nexus-publish")

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.8.1")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
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
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
        val publishing = convention.findByType(PublishingExtension::class)!!
        sign(publishing.publications)
        isRequired = isReleaseVersion
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            googleJavaFormat("1.7")
        }
        kotlin {
            ktlint("0.38.1")
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

    class ModulePathArgumentProvider(it: Project) : CommandLineArgumentProvider, Named {
        @get:CompileClasspath
        val modulePath: Configuration = it.configurations["compileClasspath"]
        override fun asArguments() = listOf("--module-path", modulePath.asPath)

        @Internal
        override fun getName() = "module-path"
    }

    class PatchModuleArgumentProvider(it: Project) : CommandLineArgumentProvider, Named {

        @get:Input
        val module: String = it.javaModuleName

        @get:InputFiles
        @get:PathSensitive(PathSensitivity.RELATIVE)
        val patch: Provider<FileCollection> = provider {
            val sourceSets = it.the<SourceSetContainer>()
            if (it == project)
                files(sourceSets.matching { it.name.startsWith("main") }.map { it.output })
            else
                files(sourceSets["main"].java.srcDirs)
        }

        override fun asArguments(): List<String> {
            val path = patch.get().filter { it.exists() }.asPath
            if (path.isEmpty()) {
                return emptyList()
            }
            return listOf("--patch-module", "$module=$path")
        }

        @Internal
        override fun getName() = "patch-module($module)"
    }

    val javaModuleName = project.javaModuleName
    val moduleSourceDir = file("src/module/$javaModuleName")
    val moduleOutputDir = file("$buildDir/classes/java/module")

    val compileModule by tasks.registering(JavaCompile::class) {
        dependsOn(tasks.named("classes"))
        source = fileTree(moduleSourceDir)
        destinationDirectory.set(moduleOutputDir)
        sourceCompatibility = "9"
        targetCompatibility = "9"
        classpath = files()
        options.release.set(9)
        options.compilerArgs.addAll(
            listOf(
                "--module-version", "${project.version}",
                "--module-source-path", files(modularProjects.map { "${it.projectDir}/src/module" }).asPath
            )
        )
        options.compilerArgumentProviders.add(ModulePathArgumentProvider(project))
        options.compilerArgumentProviders.addAll(modularProjects.map { PatchModuleArgumentProvider(it) })
        modularity.inferModulePath.set(false)
    }

    tasks {
        val replaceVersionInJava by registering {
            doLast {
                replaceVersionInArtifact(version.toString())
            }
        }

        named<JavaCompile>("compileJava") {
            dependsOn(replaceVersionInJava)
            options.encoding = encoding
        }

        named<Jar>("jar") {
            dependsOn(compileModule)
            manifest {
                attributes(mapOf("Implementation-Title" to project.name, "Implementation-Version" to archiveVersion))
            }
            from("$moduleOutputDir/$javaModuleName") {
                include("module-info.class")
            }
        }

        named<Jar>("sourcesJar") {
            from(moduleSourceDir) {
                include("module-info.java")
            }
        }

        named<Javadoc>("javadoc") {
            options.encoding = encoding
            (options as StandardJavadocDocletOptions).apply {
                charSet = encoding
                docEncoding = encoding
                use()
                exclude("**/internal/**")
            }
        }

        named<JavaCompile>("compileTestJava") {
            options.encoding = encoding
            options.compilerArgs = listOf("-proc:none")
        }

        named<Test>("test") {
            maxHeapSize = "1g"
            useJUnitPlatform()
        }

        named("build") {
            dependsOn("publishToMavenLocal")
        }

        withType<JavaCompile>().configureEach {
            modularity.inferModulePath.set(false)
        }

        withType<Sign>().configureEach {
            onlyIf { isReleaseVersion }
        }
    }
}

rootProject.apply {

    fun replaceVersionInDocs(ver: String) {
        ant.withGroovyBuilder {
            "replaceregexp"("match" to """("org.seasar.doma:doma-(core|kotlin|processor|slf4j)?:)[^"]*(")""",
                    "replace" to "\\1${ver}\\3",
                    "encoding" to encoding,
                    "flags" to "g") {
                "fileset"("dir" to ".") {
                    "include"("name" to "README.md")
                    "include"("name" to "docs/**/*.rst")
                }
            }
        }
        ant.withGroovyBuilder {
            "replaceregexp"("match" to """(<doma.version>)[^<]*(</doma.version>)""",
                    "replace" to "\\1${ver}\\2",
                    "encoding" to encoding,
                    "flags" to "g") {
                "fileset"("dir" to ".") {
                    "include"("name" to "README.md")
                }
            }
        }
    }

    tasks {
        val replaceVersion by registering {
            doLast {
                val releaseVersion = project.properties["release.releaseVersion"]?.toString()
                checkNotNull(releaseVersion)
                replaceVersionInArtifact(releaseVersion)
                replaceVersionInDocs(releaseVersion)
            }
        }

        named("beforeReleaseBuild") {
            dependsOn(replaceVersion)
        }

        named("updateVersion") {
            doLast {
                val newVersion = project.properties["version"]?.toString()
                checkNotNull(newVersion)
                replaceVersionInArtifact(newVersion)
            }
        }

        named("closeRepository") {
            onlyIf { isReleaseVersion }
        }

        named("releaseRepository") {
            onlyIf { isReleaseVersion }
        }

        register("updateChangelog") {
            doLast {
                val releaseBody: String by project
                val releaseHtmlUrl: String by project
                val releaseName: String by project
                val header = "# [${releaseName.trim('\"')}](${releaseHtmlUrl.trim('\"')})"
                val path = file("CHANGELOG.md").toPath()
                val lines = java.nio.file.Files.readAllLines(path)
                if (lines.none { it.startsWith(header) }) {
                    java.nio.file.Files.write(
                        path, listOf(header, ""),
                        java.nio.file.StandardOpenOption.WRITE,
                        java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
                    )
                    val body = releaseBody.trim('"')
                        .replace("\\\"", "\"")
                        .replace("\\n", "\n")
                        .replace("\\r", "\r")
                        .replace(
                        "#([0-9]+)".toRegex(),
                        """[$0]\(https://github.com/domaframework/doma/pull/$1\)"""
                    )
                    java.nio.file.Files.write(path, body.toByteArray(), java.nio.file.StandardOpenOption.APPEND)
                    java.nio.file.Files.write(path, listOf("", "") + lines, java.nio.file.StandardOpenOption.APPEND)
                }
            }
        }

    }

    configure<net.researchgate.release.ReleaseExtension> {
        newVersionCommitMessage = "[Gradle Release Plugin] - [skip ci] new version commit: "
    }

    configure<io.codearte.gradle.nexus.NexusStagingExtension> {
        val sonatypeUsername: String by project
        val sonatypePassword: String by project
        username = sonatypeUsername
        password = sonatypePassword
        packageGroup = "org.seasar"
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        format("misc") {
            target("**/*.gradle.kts", "**/*.gitignore")
            targetExclude("**/bin/**", "**/build/**")
            indentWithSpaces()
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("documentation") {
            target("docs/**/*.rst", "**/*.md")
            targetExclude("CHANGELOG.md")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
