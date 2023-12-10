plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.shoping"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shoping"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation ("com.google.firebase:firebase-auth:22.2.0")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")


    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.libraries.maps:maps:3.1.0-beta")
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("androidx.fragment:fragment:1.4.0")
    implementation ("androidx.cardview:cardview:1.0.0")

    implementation ("com.google.maps:google-maps-services:0.17.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")


    implementation ("com.karumi:dexter:6.2.2")
    implementation ("com.google.android.gms:play-services-location:17.1.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")  // Sử dụng Gson Converter
    implementation ("com.squareup.retrofit2:adapter-rxjava3:2.9.0")  // Sử dụng RxJava Adapter (nếu bạn muốn sử dụng RxJava)
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation ("io.reactivex.rxjava3:rxjava:3.0.13")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")  // Cập nhật phiên bản
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")  // Cập nhật phiên bản

    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("com.android.volley:volley:1.2.0")
    implementation ("androidx.fragment:fragment-ktx:1.3.0")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    implementation ("com.google.android.material:material:1.4.0")



}