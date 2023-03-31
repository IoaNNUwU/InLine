import org.jetbrains.kotlin.resolve.compatibility

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("org.jetbrains.intellij") version "1.12.0"
    id("org.jetbrains.changelog") version "2.0.0"
    kotlin("jvm") version "1.7.10"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()
}

intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
}

kotlin {
    jvmToolchain(11)
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        val myReadMe = file("README.md").readText()
                .replace("<kbd>", " `")
                .replace("</kbd>", "` ")

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
                myReadMe.lines().run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n")
                    .replace("example.png", "mini_example.png")
                    .let { org.jetbrains.changelog.markdownToHTML(it) }
        )

        changeNotes.set(
                myReadMe.lines().run {
                    val start = "<!-- Change notes -->"
                    val end = "<!-- Change notes end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Change notes section not found in README.md:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n") { it.replace("* ", "\n") }
                        .let { org.jetbrains.changelog.markdownToHTML(it) }
        )
    }
}