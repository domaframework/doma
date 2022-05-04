import java.lang.Integer.max

plugins {
    java
}

val moduleInfoFile = "$projectDir/src/main/java/module-info.java"
val useModule: Boolean = project.properties["testUseModule"].toString().toBoolean()
val javaLangVersion: Int = project.properties["testJavaLangVersion"].toString().toInt()

delete(moduleInfoFile)
if (useModule) {
    file(moduleInfoFile).writeText("""
                open module org.seasar.doma.it.java.latest {
                    requires org.seasar.doma.core;
                }
            """.trimIndent()
    )
}

dependencies {
    annotationProcessor(project(":doma-processor"))
    implementation(project(":doma-core"))
    testImplementation(project(":integration-test-common"))
}

java {
    val version = max(17, javaLangVersion)
    toolchain.languageVersion.set(JavaLanguageVersion.of(version))
}
