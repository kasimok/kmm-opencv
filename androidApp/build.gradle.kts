plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.example.kmmopencv.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.kmmopencv.android"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation("org.opencv:opencv:4.10.0")
}
