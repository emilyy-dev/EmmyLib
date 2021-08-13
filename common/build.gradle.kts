repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

val brigadierVersion: String = "1.0.18"
val authlibVersion: String = "2.2.30"
val snakeyamlVersion: String = "1.28"
val moshiVersion: String = "1.12.0"
val gsonVersion: String = "2.8.7"
val caffeineVersion: String = "3.0.2"
val guavaVersion: String = "30.1.1-jre"
val slf4jVersion: String = "1.8.0-beta4"
val log4jVersion: String = "2.14.1"
val annotationsVersion: String = "21.0.1"
val adventureVersion: String = "4.8.1"
val adventurePlatformVersion: String = "4.0.0-SNAPSHOT"

dependencies {
    compileOnlyApi("com.mojang", "brigadier", brigadierVersion)
    compileOnlyApi("com.mojang", "authlib", authlibVersion)

    api("org.yaml", "snakeyaml", snakeyamlVersion)
    api("com.squareup.moshi", "moshi", moshiVersion)
    api("com.google.code.gson", "gson", gsonVersion)

    api("com.github.ben-manes.caffeine", "caffeine", caffeineVersion)

    api("com.google.guava", "guava", guavaVersion)

    api("org.slf4j", "slf4j-api", slf4jVersion)
    api("org.apache.logging.log4j", "log4j-api", log4jVersion)
    runtimeOnly("org.apache.logging.log4j", "log4j-core", log4jVersion)
    runtimeOnly("org.apache.logging.log4j", "log4j-slf4j18-impl", log4jVersion)

    api("org.jetbrains", "annotations", annotationsVersion)

    api("net.kyori", "adventure-api", adventureVersion)
    api("net.kyori", "adventure-text-serializer-gson", adventureVersion)
    api("net.kyori", "adventure-text-serializer-plain", adventureVersion)
    api("net.kyori", "adventure-platform-api", adventurePlatformVersion)

    testImplementation("com.mojang", "brigadier", brigadierVersion)
}

tasks.javadoc {
    val standardOptions = options as StandardJavadocDocletOptions
    options.encoding = Charsets.UTF_8.name()
//    standardOptions.addBooleanOption("-no-module-directories", true)
    listOf(
        "https://docs.oracle.com/en/java/javase/11/docs/api/",
        "https://square.github.io/moshi/1.x/moshi/",
        "https://jd.adventure.kyori.net/api/$adventureVersion/",
        "https://jd.adventure.kyori.net/text-serializer-gson/$adventureVersion/",
        "https://jd.adventure.kyori.net/text-serializer-plain/$adventureVersion/",
        "https://javadoc.io/doc/com.google.code.gson/gson/$gsonVersion/",
        "https://javadoc.io/doc/org.yaml/snakeyaml/$snakeyamlVersion/",
        "https://javadoc.io/doc/com.google.guava/guava/$guavaVersion/",
        "https://javadoc.io/doc/org.slf4j/slf4j-api/$slf4jVersion/",
        "https://javadoc.io/doc/org.apache.logging.log4j/log4j-api/$log4jVersion/",
        "https://javadoc.io/doc/org.jetbrains/annotations/$annotationsVersion/"
    ).forEach { standardOptions.links?.add(it) }
}