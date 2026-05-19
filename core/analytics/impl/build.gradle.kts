plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)

}

android {
    namespace = "com.example.analytics.impl"

}

dependencies {
    implementation(project(path = ":core:analytics:api"))
    implementation(project(path = ":core:build-config:api"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.firebase.analytics)
}