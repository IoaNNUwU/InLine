fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("org.jetbrains.intellij") version "1.12.0"
    id("org.jetbrains.changelog") version "2.0.0"
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

changelog {
    groups.set(emptyList())
    repositoryUrl.set(properties("pluginRepositoryUrl"))
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
                file("README.md").readText().lines().run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n").let { org.jetbrains.changelog.markdownToHTML(it) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(provider {
            with(changelog) {
                renderItem(
                        getOrNull(properties("pluginVersion"))
                                ?: runCatching { getLatest() }.getOrElse { getUnreleased() },
                        org.jetbrains.changelog.Changelog.OutputType.HTML,
                )
            }
        })
    }
}