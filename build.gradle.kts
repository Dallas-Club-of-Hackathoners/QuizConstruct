// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    extensions.configure<ExtraPropertiesExtension> {
        set("targetAndroidSdk", 34)
        set("minAndroidSdk", 24)
    }

}
