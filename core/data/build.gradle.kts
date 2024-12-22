plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.andyc.core.data"
}

dependencies {
    implementation(libs.timber)

    implementation(projects.core.domain)
    implementation(projects.core.database)
}