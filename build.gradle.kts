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

val javaLangVersion: Int = project.properties["javaLangVersion"].toString().toInt()
val testJavaLangVersion: Int = project.properties["testJavaLangVersion"].toString().toInt()

val modularProjects: List<Project> = subprojects.filter { it.name.startsWith("doma-") }
val integrationTestProjects: List<Project> = subprojects.filter { it.name.startsWith("integration-test-") }
val unitTestProjects: List<Project> = subprojects.filter { it.name.startsWith("unit-test") }

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
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.11.0")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    }

    spotless {
        java {
            googleJavaFormat("1.23.0")
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
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaLangVersion))
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
                    description.set("DAO Oriented Database Mapping Framework for Java 17+")
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
            manifest {
                attributes(mapOf("Implementation-Title" to project.name, "Implementation-Version" to archiveVersion))
            }
        }

        javadoc {
            options.encoding = encoding
            (options as StandardJavadocDocletOptions).apply {
                charSet = encoding
                docEncoding = encoding
                use()
                addStringOption("Xdoclint:none", "-quiet")
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
    apply(plugin = "org.domaframework.doma.compile")

    dependencies {
        testImplementation(platform("org.testcontainers:testcontainers-bom:1.20.1"))
        testRuntimeOnly("com.h2database:h2:2.3.232")
        testRuntimeOnly("mysql:mysql-connector-java:8.0.33")
        testRuntimeOnly("com.oracle.database.jdbc:ojdbc11-production:23.5.0.24.07")
        testRuntimeOnly("org.postgresql:postgresql:42.7.4")
        testRuntimeOnly("com.microsoft.sqlserver:mssql-jdbc:12.8.1.jre11")
        testRuntimeOnly("org.testcontainers:mysql")
        testRuntimeOnly("org.testcontainers:oracle-xe")
        testRuntimeOnly("org.testcontainers:postgresql")
        testRuntimeOnly("org.testcontainers:mssqlserver")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(testJavaLangVersion))
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = encoding
        }

        compileJava {
            options.incrementalAfterFailure.set(false)
        }

        compileTestJava {
            options.compilerArgs.addAll(listOf("-proc:none"))
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

configure(unitTestProjects) {
    apply(plugin = "java")
    apply(plugin = "com.diffplug.spotless")

    tasks {
        test {
            useJUnitPlatform()
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
