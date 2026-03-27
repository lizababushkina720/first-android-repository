plugins {
    alias(libs.plugins.app.android.application)
    alias(libs.plugins.app.compose)
}

android {
    namespace = "com.example.hw_01_sem2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.hw_01_sem2"
        versionCode = 1
        versionName = "1.0"

    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(project(path=":core:data"))
    implementation(project(path=":core:domain"))
    implementation(project(path=":core:data"))
    implementation(project(path=":core:build-config:api"))
    implementation(project(path=":core:network"))
    implementation(project(path=":core:build-config:impl"))
    implementation(project(path=":core:di"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.x.lifecycle.runtime.ktx)
    implementation(libs.x.activity.compose)
    implementation(libs.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation("androidx.compose.material:material-icons-extended:1.7.0")
    implementation("androidx.compose.material:material:1.7.0")

    implementation("com.google.code.gson:gson:2.11.0")
}