import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.intellij") version "1.12.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.20"
    id("org.jetbrains.compose").version("1.5.10")
}

group = "io.github.vacxe"
version = "1.0.3"

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/kpm/public/")
}

intellij {
    version.set("2023.3.1")
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
        sinceBuild.set("233")
        untilBuild.set("")
        changeNotes.set(
            """
            Write UI with Compose, just for kicks.
        """
        )
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
    implementation("org.jetbrains.jewel:jewel-ide-laf-bridge:0.12.0-ij-233") {
        exclude(group = "org.jetbrains.kotlinx")
    }
    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material")
        exclude(group = "org.jetbrains.kotlinx")
    }
}
