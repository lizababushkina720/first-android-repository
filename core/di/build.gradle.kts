plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.app.room)
}

android {
    namespace = "com.example.di"

}

dependencies {
    implementation(project(path = ":core:network"))
    implementation(project(path = ":core:domain"))
    implementation(project(path = ":core:data"))
    implementation(project(path = ":core:build-config:api"))
    implementation(project(path = ":core:build-config:impl"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
}