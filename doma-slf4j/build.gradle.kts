description = "doma-slf4j"

dependencies {
    api("org.slf4j:slf4j-api:2.0.16")
    implementation(project(":doma-core"))
    testImplementation("ch.qos.logback:logback-classic:1.5.9")
}
