plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.android.junit5)
}

android {
    namespace = "com.andyc.core.android_test"
}

dependencies {
    implementation(libs.ktor.client.mock)
    implementation(libs.bundles.ktor)
    implementation(libs.coroutines.test)

    implementation(projects.auth.data)
    implementation(projects.core.domain)
    api(projects.core.test)
}