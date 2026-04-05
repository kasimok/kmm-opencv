plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "21"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
        target.compilations.getByName("main") {
            val OpenCVWrapper by cinterops.creating {
                defFile("src/iosMain/cinterop/OpenCVWrapper.def")
                includeDirs("${project.rootDir}/iosApp/OpenCVWrapper")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {}
        androidMain.dependencies {
            implementation("org.opencv:opencv:4.10.0")
        }
    }
}

android {
    namespace = "com.example.kmmopencv.shared"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
