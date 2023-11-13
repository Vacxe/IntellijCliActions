plugins {
    java
    kotlin("jvm")
    id("org.jetbrains.intellij") version "1.12.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.20"
    id("org.jetbrains.compose")
}

group = "io.github.vacxe"
version = "1.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

intellij {
    version.set("2022.2.1")
    pluginName.set("CLI Actions")
    plugins.set(listOf("org.jetbrains.plugins.terminal"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.charleskorn.kaml:kaml:0.54.0")
}
