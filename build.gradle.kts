import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.dokka") version "0.10.0"
    id("maven-publish")
}

group = "atrico.kotlib"
version = "1.0.0"


val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "$buildDir/dokka"
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(dokka)
}

publishing {
    publications {
        create<MavenPublication>("atrico.kotlib.testing") {
            from(components["java"])
            artifact(dokkaJar)
        }
    }

    repositories {
        maven {
            name = "local"
            url = uri("file://$buildDir/repository")
        }
    }
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(group = "com.natpryce", name = "hamkrest", version = "1.7.0.0")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.5.2")
    testImplementation(group = "com.natpryce", name = "hamkrest", version = "1.7.0.0")
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
     }
}




