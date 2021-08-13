defaultTasks("licenseMain", "build")

plugins {
    `java-library`
    `maven-publish`
    id("com.github.hierynomus.license-base") version "0.16.1"
}

project.group = "io.github.emilyy-dev"
project.version = "0.1-SNAPSHOT"

subprojects {

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.hierynomus.license-base")

    project.group = rootProject.group
    project.version = rootProject.version

    java {
        withJavadocJar()
        withSourcesJar()
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }

    license {
        header = rootProject.file("header.txt")
        encoding = "UTF-8"
        mapping("java", "DOUBLESLASH_STYLE")
        include("**/*.java")
    }

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(11)
        }

        jar {
            metaInf {
                from(rootProject.file("LICENSE.txt")) { into("") }
            }
        }

        test {
            useJUnitPlatform()
        }

        compileTestJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(11)
        }
    }

    dependencies {
        testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.7.2")
        testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.7.2")
    }
}
