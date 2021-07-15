// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven(uri("https://jitpack.io" ) )
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0-alpha03")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}
allprojects {
    repositories {
       // maven(url = "https://jitpack.io")
        maven(uri("https://jitpack.io" ) )
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
