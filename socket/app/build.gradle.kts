plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gnoam.socket"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.gnoam.socket"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

//    implementation ("io.socket:socket.io-client:2.0.0")

    implementation ("io.socket:socket.io-client:2.0.0") {
        exclude(group = "org.json", module = "json")
    }

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // RxJava
    implementation ("io.reactivex.rxjava2:rxandroid:2.0.2")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.18")
//    implementation group: 'com.uber.rxdogtag', name: 'rxdogtag', version: '1.0.0'
    implementation("com.uber.rxdogtag:rxdogtag:1.0.0")

    implementation ("org.apache.commons:commons-lang3:3.11")
}