import com.android.build.api.dsl.BuildFeatures

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.quranmentor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quranmentor"
        minSdk = 26
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
    sourceSets {
        getByName("main") {
            res {
                srcDirs("src\\main\\res", "src\\main\\res\\sublayouts")
            }
        }
    }

}
dependencies {
    // Android UI and Material Design
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // Firebase
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.google.android.gms:play-services-auth:19.2.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation ("com.google.firebase:firebase-analytics:19.0.2")
    // DataStore
    implementation("androidx.datastore:datastore-core-android:1.1.1")
    // Activity and Lifecycle
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.activity:activity-compose:1.3.0-alpha05")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.compose.ui:ui-tooling:1.0.0-beta01") {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-compose-desktop")
    }
    implementation("androidx.core:core-ktx:+")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Image handling
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    // Flexbox layout
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    // Networking
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("org.json:json:20210307")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // Firebase BOM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    //Stripe
    // Stripe Android SDK
    implementation ("com.stripe:stripe-android:20.48.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation ("com.google.android.libraries.places:places:2.6.0")

    // Compose
    implementation ("androidx.compose.ui:ui-tooling:1.0.0-beta01")
    // Location services
    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation ("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:+")

    implementation("com.github.AnyChart:AnyChart-Android:1.1.2")
    implementation ("com.intuit.sdp:sdp-android:1.1.1")

    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("com.airbnb.android:lottie:6.4.1")

}



