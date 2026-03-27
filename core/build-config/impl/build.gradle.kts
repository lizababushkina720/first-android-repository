plugins {
    alias(libs.plugins.app.android.library)
}

android {
    namespace = "com.example.impl"

    buildFeatures {
        buildConfig=true
    }

    defaultConfig{
        buildConfigField("String","DADATA_API_BASE_URL","\"https://suggestions.dadata.ru/suggestions/api/4_1/rs/\"")
        buildConfigField("String","DADATA_API_KEY","\"Token 0de473d7999a7b38a7486e0e3d648e546e25af69\"")
    }

}

dependencies {
    implementation(project(path=":core:build-config:api"))
}