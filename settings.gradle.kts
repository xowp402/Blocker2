pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(
        RepositoriesMode.FAIL_ON_PROJECT_REPOS
    )

    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Blocker2"

include(
    ":app",

    ":core:common",
    ":core:data",

    ":engine:wordfilter",
    ":engine:websitefilter",
    ":engine:blocking",

    ":service:accessibility",
    ":service:monitoring",
    ":service:recovery"
)
