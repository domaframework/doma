plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.spotless)
    alias(libs.plugins.publish)
    alias(libs.plugins.release)
    alias(libs.plugins.doma.compile)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

val javaLangVersion: Int = project.properties["javaLangVersion"].toString().toInt()
val testJavaLangVersion: Int = project.properties["testJavaLangVersion"].toString().toInt()

val modularProjects: List<Project> = subprojects.filter { it.name.startsWith("doma-") }
val integrationTestProjects: List<Project> = subprojects.filter { it.name.startsWith("integration-test-") }

val encoding: String by project
val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")

// Retain a reference to rootProject.libs to make the version catalog accessible within allprojects and subprojects.
// See https://github.com/gradle/gradle/issues/16708
val catalog = libs

data class Replacement(
    val match: String,
    val replace: String,
    val dir: String = ".",
    val file: String,
)

fun replace(replacements: List<Replacement>) {
    replacements.forEach { r ->
        ant.withGroovyBuilder {
            "replaceregexp"(
                buildMap {
                    put("match", r.match)
                    put("replace", r.replace)
                    put("encoding", encoding)
                    put("flags", "g")
                }
            ) {
                "fileset"("dir" to r.dir) {
                    "include"("name" to r.file)
                }
            }
        }
    }
}

fun replaceVersionInArtifact(ver: String) {
    val replacement = Replacement(
        """(private static final String VERSION = ")[^"]*(")""",
        "\\1${ver}\\2",
        file = "**/Artifact.java",
    )
    replace(listOf(replacement))
}

fun replaceVersionInDocs(ver: String) {
    val replacements = listOf(
        // README.md replacements
        Replacement(
            """("org.seasar.doma:doma-(core|kotlin|processor|slf4j|template)?:)[^"]*(")""",
            "\\1${ver}\\3",
            file = "README.md",
        ),
        Replacement(
            """(<doma.version>)[^<]*(</doma.version>)""",
            "\\1${ver}\\2",
            file = "README.md",
        ),
        Replacement(
            """(id\("org.domaframework.doma.compile"\) version ")[^"]*(")""",
            "\\1${catalog.plugins.doma.compile.get().version}\\2",
            file = "README.md",
        ),
        Replacement(
            "(\\|\\s*H2 Database\\s*\\|\\s*)[^\\s]*(\\s*\\|)",
            "\\1${catalog.jdbc.h2.get().version}\\2",
            file = "README.md",
        ),
        Replacement(
            "(\\|\\s*SQLite\\s*\\|\\s*)[^\\s]*(\\s*\\|)",
            "\\1${catalog.jdbc.sqlite.get().version}\\2",
            file = "README.md",
        ),

        // conf.py replacements
        Replacement(
            "('doma_version'\\s*:\\s*')[^']*(')",
            "\\1${ver}\\2",
            dir = "docs",
            file = "conf.py",
        ),
        Replacement(
            "('doma_compile_version'\\s*:\\s*')[^']*(')",
            "\\1${catalog.plugins.doma.compile.get().version}\\2",
            dir = "docs",
            file = "conf.py",
        ),
        Replacement(
            "('doma_codegen_version'\\s*:\\s*')[^']*(')",
            "\\1${catalog.plugins.doma.codegen.get().version}\\2",
            dir = "docs",
            file = "conf.py",
        ),
        Replacement(
            "('eclipse_apt_version'\\s*:\\s*')[^']*(')",
            "\\1${catalog.plugins.eclipse.apt.get().version}\\2",
            dir = "docs",
            file = "conf.py",
        ),
        Replacement(
            "('logback_classic_version'\\s*:\\s*')[^']*(')",
            "\\1${catalog.logback.classic.get().version}\\2",
            dir = "docs",
            file = "conf.py",
        ),
        Replacement(
            "('quarkus_doma_version'\\s*:\\s*')[^']*(')",
            "\\1${catalog.quarkus.doma.get().version}\\2",
            dir = "docs",
            file = "conf.py",
        ),
    )
    replace(replacements)
}

allprojects {
    apply(plugin = "base")
    apply(plugin = catalog.plugins.spotless.get().pluginId)

    repositories {
        mavenCentral()
    }

    spotless {
        val licenseHeaderFile = rootProject.file("spotless/copyright.java")

        lineEndings = com.diffplug.spotless.LineEnding.UNIX
        java {
            googleJavaFormat(catalog.google.java.format.get().version)
            licenseHeaderFile(licenseHeaderFile)
        }
        // https://github.com/diffplug/spotless/issues/532
        format("javaMisc") {
            target("src/**/package-info.java", "src/**/module-info.java")
            licenseHeaderFile(licenseHeaderFile, "(package|module|\\/\\*\\*)")
        }
        kotlin {
            ktlint(catalog.ktlint.get().version)
            licenseHeaderFile(licenseHeaderFile)
        }
        kotlinGradle {
            ktlint(catalog.ktlint.get().version)
        }
        format("misc") {
            target("*.md", "doma-core/.md")
            leadingTabsToSpaces()
            trimTrailingWhitespace()
            endWithNewline()
        }
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
        testImplementation(platform(catalog.junit.bom))
        testImplementation(catalog.junit.jupiter.api)
        testRuntimeOnly(catalog.junit.platform.launcher)
        testRuntimeOnly(catalog.junit.jupiter.engine)
        testRuntimeOnly(catalog.apiguardian)
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
                    description.set("DAO Oriented Database Mapping Framework for Java")
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
                        connection.set("scm:git:$githubUrl")
                        developerConnection.set("scm:git:$githubUrl")
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
            replaceVersionInArtifact(version.toString())
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

        withType<Sign>().configureEach {
            onlyIf { isReleaseVersion }
        }
    }
}

configure(integrationTestProjects) {
    apply(plugin = "java")
    apply(plugin = catalog.plugins.doma.compile.get().pluginId)

    dependencies {
        testImplementation(platform(catalog.testcontainers.bom))
        testRuntimeOnly(catalog.jdbc.h2)
        testRuntimeOnly(catalog.jdbc.mysql)
        testRuntimeOnly(catalog.jdbc.oracle)
        testRuntimeOnly(catalog.jdbc.postgresql)
        testRuntimeOnly(catalog.jdbc.sqlite)
        testRuntimeOnly(catalog.jdbc.sqlserver)
        testRuntimeOnly(catalog.testcontainers.mysql)
        testRuntimeOnly(catalog.testcontainers.oracle)
        testRuntimeOnly(catalog.testcontainers.postgresql)
        testRuntimeOnly(catalog.testcontainers.sqlserver)
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(testJavaLangVersion))
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = encoding
        }

        compileJava {
            val ap: String by project
            ap.split(",").map { it.trim() }.filter { it.isNotEmpty() }.map { "-A$it" }.forEach {
                options.compilerArgs.add(it)
            }
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

        val sqlite by registering(Test::class) {
            prepare("sqlite")
        }

        val sqlserver by registering(Test::class) {
            prepare("sqlserver")
        }

        register("testAll") {
            dependsOn(h2, mysql, oracle, postgresql, sqlite, sqlserver)
        }
    }
}

rootProject.apply {
    release {
        newVersionCommitMessage.set("[Gradle Release Plugin] - [skip ci] new version commit: ")
        git {
            requireBranch.set("")
        }
    }

    nexusPublishing {
        repositories {
            // see https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/#configuration
            sonatype {
                nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
                snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            }
        }
        packageGroup.set("org.seasar")
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
