plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
}

android {
    namespace = "com.example.network"

}

dependencies {
    implementation(project(path = ":core:build-config:api"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)


}