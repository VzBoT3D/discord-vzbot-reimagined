val h2_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.21"
    id("io.ktor.plugin") version "3.0.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
}

val exposedVersion = "0.56.0"

group = "org.vzbot"
version = "0.0.1"

application {
    mainClass.set("org.vzbot.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(21)
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")  // Specify the Kotlin source directory only
        java.srcDirs()  // Remove the default Java source directory
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://packages.confluent.io/maven/") }
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-cio-jvm")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("com.zaxxer:HikariCP:6.0.0")

    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("com.zellerfeld.development:ZellerBot-API:1.0-SNAPSHOT")
    implementation ("com.github.StaticFX:ktor-middleware:b02796db1b")
    implementation("io.ktor:ktor-client-cio-jvm:3.0.1")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.1")
    implementation("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.4")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("com.github.StaticFX.kotlin-exposed-relationships:annotations:1.0.2")
    ksp("com.github.StaticFX.kotlin-exposed-relationships:processor:1.0.2")
}
