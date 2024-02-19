pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

}

rootProject.name = "QuizConsruct"
include(":app")
include(":features:main")
include(":core:common")
include(":core:android")
include(":navigation")
