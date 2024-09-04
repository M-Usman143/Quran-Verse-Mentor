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
        jcenter()

        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://storage.zego.im/maven") }
        //maven { url = uri("https://jcentre.bintray.com") }
    }
}

rootProject.name = "QuranMentor"
include(":app")
