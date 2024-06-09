buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.google.services)
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}