import java.util.Properties

plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
}
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
android {
    namespace = "com.example.buildconfig.impl"

    buildFeatures {
        buildConfig=true
    }

    defaultConfig{
        buildConfigField("String","DADATA_API_BASE_URL","\"https://suggestions.dadata.ru/suggestions/api/4_1/rs/\"")
        buildConfigField(type = "String", name = "DADATA_API_KEY", value = "\"${localProperties.getProperty("DADATA_API_KEY", "")}\"")
    }

}

dependencies {
    implementation(project(path=":core:build-config:api"))
}