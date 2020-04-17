plugins {
	base
	id("com.diffplug.eclipse.apt") version "3.22.0" apply false
	id("com.diffplug.gradle.spotless") version "3.27.2"
	id("de.marcphilipp.nexus-publish") version "0.4.0" apply false
	id("net.researchgate.release") version "2.8.1"
}

val encoding: String by project
val isSnapshot = project.version.toString().endsWith("SNAPSHOT")
val secretKeyRingFile: String? =
		findProperty("signing.secretKeyRingFile")?.toString()?.let {
			if (it.isEmpty()) null
			else file(it).absolutePath
		}

allprojects {
	val replaceVersionJava by tasks.registering {
		doLast {
			ant.withGroovyBuilder {
				"replaceregexp"("match" to """(private static final String VERSION = ")[^"]*(")""",
						"replace" to "\\1${version}\\2",
						"encoding" to encoding,
						"flags" to "g") {
					"fileset"("dir" to ".") {
						"include"("name" to "**/Artifact.java")
					}
				}
			}
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
	apply(plugin = "com.diffplug.gradle.spotless")
	apply(plugin = "de.marcphilipp.nexus-publish")

	extra["signing.secretKeyRingFile"] = secretKeyRingFile

	val compileJava by tasks.existing(JavaCompile::class) {
		dependsOn(tasks.named("replaceVersionJava"))
		options.encoding = encoding
	}

	val compileTestJava by tasks.existing(JavaCompile::class) {
		options.encoding = encoding
		options.compilerArgs = listOf("-proc:none")
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
			attributes(mapOf("Implementation-Title" to project.name, "Implementation-Version" to archiveVersion))
		}
	}

	val test by tasks.existing(Test::class) {
		maxHeapSize = "1g"
		useJUnitPlatform()
	}

	val build by tasks.existing {
		val publishToMavenLocal by tasks.existing
		dependsOn(publishToMavenLocal)
	}

	dependencies {
		"testImplementation"("org.junit.jupiter:junit-jupiter-api:5.4.0")
		"testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.4.0")
	}

	configure<JavaPluginExtension> {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
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

	configure<com.diffplug.gradle.spotless.SpotlessExtension> {
		java {
			googleJavaFormat("1.7")
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

rootProject.apply {
	apply(from = "release.gradle")

	val replaceVersionDoc by tasks.registering {
		mustRunAfter(tasks.named("updateVersion"))
		doLast {
			ant.withGroovyBuilder {
				"replaceregexp"("match" to """("org.seasar.doma:doma(-core|-processor)?:)[^"]*(")""",
						"replace" to "\\1${version}\\3",
						"encoding" to encoding,
						"flags" to "g") {
					"fileset"("dir" to ".") {
						"include"("name" to "README.md")
						"include"("name" to "docs/**/*.rst")
					}
				}
			}
		}
	}

	val replaceVersion by tasks.registering {
		dependsOn(replaceVersionDoc, tasks.named("replaceVersionJava"))
	}

	val commitNewVersion by tasks.existing {
		dependsOn(replaceVersion)
	}

	configure<com.diffplug.gradle.spotless.SpotlessExtension> {
		format("misc") {
			target("**/*.gradle", "**/*.gradle.kts", "**/*.gitignore")
			targetExclude("**/bin/**", "**/build/**")
			indentWithTabs()
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
