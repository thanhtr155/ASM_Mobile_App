plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.btec.fpt.campus_expense_manager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.btec.fpt.campus_expense_manager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // JUnit
    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.core)
    // Espresso for UI testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}