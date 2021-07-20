description = "doma-slf4j"

dependencies {
    api("org.slf4j:slf4j-api:1.7.32")
    implementation(project(":doma-core"))
    testImplementation("ch.qos.logback:logback-classic:1.2.3")
}
