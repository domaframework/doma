plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.diffplug.spotless")
    id("io.github.gradle-nexus.publish-plugin")
    id("net.researchgate.release")
    id("org.domaframework.doma.compile")
    kotlin("jvm")
    kotlin("kapt")
}

val Project.javaModuleName: String
    get() = "org.seasar." + this.name.replace('-', '.')

val modularProjects: List<Project> = subprojects.filter { it.name.startsWith("doma-") }
val integrationTestProjects: List<Project> = subprojects.filter { it.name.startsWith("integration-test-") }

val encoding: String by project
val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")

fun replaceVersionInArtifact(ver: String) {
    ant.withGroovyBuilder {
        "replaceregexp"(
            "match" to """(private static final String VERSION = ")[^"]*(")""",
            "replace" to "\\1${ver}\\2",
            "encoding" to encoding,
            "flags" to "g"
        ) {
            "fileset"("dir" to ".") {
                "include"("name" to "**/Artifact.java")
            }
        }
    }
}

fun replaceVersionInDocs(ver: String) {
    ant.withGroovyBuilder {
        "replaceregexp"(
            "match" to """("org.seasar.doma:doma-(core|kotlin|processor|slf4j|template)?:)[^"]*(")""",
            "replace" to "\\1${ver}\\3",
            "encoding" to encoding,
            "flags" to "g"
        ) {
            "fileset"("dir" to ".") {
                "include"("name" to "README.md")
                "include"("name" to "docs/**/*.rst")
            }
        }
    }
    ant.withGroovyBuilder {
        "replaceregexp"(
            "match" to """(<doma.version>)[^<]*(</doma.version>)""",
            "replace" to "\\1${ver}\\2",
            "encoding" to encoding,
            "flags" to "g"
        ) {
            "fileset"("dir" to ".") {
                "include"("name" to "README.md")
            }
        }
    }
}

allprojects {
    apply(plugin = "base")
    apply(plugin = "com.diffplug.spotless")

    repositories {
        mavenCentral()
    }

    tasks {
        build {
            dependsOn(spotlessApply)
        }
    }
}

subprojects {
    apply(plugin = "java")

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.10.3")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.10.3")
    }

    spotless {
        java {
            googleJavaFormat("1.17.0")
        }
        kotlin {
            ktlint("0.48.2")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}

configure(modularProjects) {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
        withJavadocJar()
        withSourcesJar()
    }

    publishing {
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

    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
        val publishing = extensions.getByType(PublishingExtension::class)
        sign(publishing.publications)
        isRequired = isReleaseVersion
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
    val moduleOutputDir = layout.buildDirectory.dir("classes/java/module")

    val compileModule by tasks.registering(JavaCompile::class) {
        dependsOn("classes")
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

        compileJava {
            dependsOn(replaceVersionInJava)
            options.encoding = encoding
        }

        jar {
            dependsOn(compileModule)
            manifest {
                attributes(mapOf("Implementation-Title" to project.name, "Implementation-Version" to archiveVersion))
            }
            from("${moduleOutputDir.get().asFile}/$javaModuleName") {
                include("module-info.class")
            }
        }

        named<Jar>("sourcesJar") {
            from(moduleSourceDir) {
                include("module-info.java")
            }
        }

        javadoc {
            options.encoding = encoding
            (options as StandardJavadocDocletOptions).apply {
                charSet = encoding
                docEncoding = encoding
                use()
                exclude("**/internal/**")
            }
        }

        compileTestJava {
            options.encoding = encoding
            options.compilerArgs = listOf("-proc:none")
        }

        test {
            maxHeapSize = "1g"
            useJUnitPlatform()
        }

        build {
            dependsOn("publishToMavenLocal")
        }

        withType<Sign>().configureEach {
            onlyIf { isReleaseVersion }
        }
    }
}

configure(integrationTestProjects) {
    apply(plugin = "java")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin ="org.domaframework.doma.compile")

    dependencies {
        testImplementation(platform("org.testcontainers:testcontainers-bom:1.19.8"))
        testRuntimeOnly("com.h2database:h2:1.4.200")
        testRuntimeOnly("mysql:mysql-connector-java:8.0.33")
        testRuntimeOnly("com.oracle.database.jdbc:ojdbc8-production:18.15.0.0")
        testRuntimeOnly("org.postgresql:postgresql:42.7.3")
        testRuntimeOnly("com.microsoft.sqlserver:mssql-jdbc:8.4.1.jre8")
        testRuntimeOnly("org.testcontainers:mysql")
        testRuntimeOnly("org.testcontainers:oracle-xe")
        testRuntimeOnly("org.testcontainers:postgresql")
        testRuntimeOnly("org.testcontainers:mssqlserver")
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = encoding
        }

        fun Test.prepare(driver: String) {
            val urlKey = "$driver.url"
            val url = project.property(urlKey) ?: throw GradleException("The $urlKey property is not found.")
            this.systemProperty("driver", driver)
            this.systemProperty("url", url)
            maxHeapSize = "1g"
            useJUnitPlatform()
        }

        test {
            val driver: Any by project
            prepare(driver.toString())
        }

        val h2 by registering(Test::class) {
            prepare("h2")
        }

        val mysql by registering(Test::class) {
            prepare("mysql")
        }

        val mysql8 by registering(Test::class) {
            prepare("mysql8")
        }

        val oracle by registering(Test::class) {
            prepare("oracle")
        }

        val postgresql by registering(Test::class) {
            prepare("postgresql")
        }

        val sqlserver by registering(Test::class) {
            prepare("sqlserver")
        }

        register("testAll") {
            dependsOn(h2, mysql, oracle, postgresql, sqlserver)
        }
    }
}

rootProject.apply {
    release {
        newVersionCommitMessage.set("[Gradle Release Plugin] - [skip ci] new version commit: ")
        git {
            requireBranch.set("master")
        }
    }

    nexusPublishing {
        repositories {
            sonatype()
        }
        packageGroup.set("org.seasar")
    }

    spotless {
        format("misc") {
            target("**/*.gradle.kts", "**/*.gitignore")
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

    tasks {
        val replaceVersion by registering {
            doLast {
                val releaseVersion = project.properties["release.releaseVersion"]?.toString()
                checkNotNull(releaseVersion)
                replaceVersionInArtifact(releaseVersion)
                replaceVersionInDocs(releaseVersion)
            }
        }

        beforeReleaseBuild {
            dependsOn(replaceVersion)
        }

        updateVersion {
            doLast {
                val newVersion = project.properties["version"]?.toString()
                checkNotNull(newVersion)
                replaceVersionInArtifact(newVersion)
            }
        }
    }
}
