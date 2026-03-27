plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.app.room)
}

android {
    namespace = "com.example.data"

}

dependencies {
    implementation(project(path = ":core:build-config:api"))
    implementation(project(path = ":core:build-config:impl"))

    implementation(project(path = ":core:domain"))
    implementation(project(path = ":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("com.google.code.gson:gson:2.11.0")


    implementation(libs.retrofit)
}