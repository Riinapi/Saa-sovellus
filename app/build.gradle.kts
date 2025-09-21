plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.myweatherdiary"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myweatherdiary"
        minSdk = 24
        targetSdk = 35
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    // Google Play Services Location -kirjasto
    // Tarjoaa sijaintipalvelut, kuten laitteen sijainnin hakemisen.
    implementation("com.google.android.gms:play-services-location:21.3.0")
    // Volley HTTP -kirjasto
    // Helpottaa HTTP-pyyntöjen lähettämistä ja verkon tietojen käsittelyä.
    implementation("com.android.volley:volley:1.2.1")
    // Glide image loading library
    // Lataa ja näyttää kuvia tehokkaasti URL-osoitteista.
    implementation("com.github.bumptech.glide:glide:4.15.1")
    // Lifecycle ViewModel -kirjasto (KTX)
    // ViewModel pitää sovelluksen tilan elossa aktiviteettien ja fragmenttien elinkaaren yli.
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    // Lifecycle LiveData -kirjasto (KTX)
    // LiveData seuraa dataa ja päivittää UI:n automaattisesti muutosten mukaan.
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")



}