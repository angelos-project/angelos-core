import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    application
}

repositories {
    mavenCentral()
}

/*val angelosCoreClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}*/

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    api(project(":lib"))
    /*angelosCoreClasspath(project(mapOf(
            "path" to ":producer",
            "configuration" to "instrumentedJars")))*/
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ServerKt")
    mainClass.set("ClientKt")
}