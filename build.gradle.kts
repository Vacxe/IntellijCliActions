import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.intellij") version "1.12.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.20"
}

group = "io.github.vacxe"
version = "1.0.6"

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.2.2")
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
        sinceBuild.set("232")
        untilBuild.set("")
        changeNotes.set("""
            Migrate from deprecated Terminal View. Tab names support and reuse tabs.
        """)
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
    implementation("com.charleskorn.kaml:kaml:0.54.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
