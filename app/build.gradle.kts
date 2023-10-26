plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    /*id("com.google.devtools.ksp")*/
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.filmyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.filmyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }

        resourceConfigurations += arrayOf("en", "de", "es", "hi", "nl")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            //isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            /*buildConfigField("String", "TMDB_API_KEY - ad45629d1f8d387910cd4ccfbb72daea", "\"" + getTMDBApiKey() + "\"")
            buildConfigField("String", "OMDB_API_KEY - 35758101", "\"" + getOMDBApiKey() + "\"")
            buildConfigField("String", "YOUTUBE_API_KEY - AIzaSyBQqQL1dfVZV3KwF0bh_T_qeJZahTeHAUc", "\"" + getYoutubeApiKey() + "\"")*/
        }

        /*debug {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "TMDB_API_KEY", "\"" + getTMDBApiKey() + "\"")
            buildConfigField("String", "OMDB_API_KEY", "\"" + getOMDBApiKey() + "\"")
            buildConfigField("String", "YOUTUBE_API_KEY", "\"" + getYoutubeApiKey() + "\"")
        }*/
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

/*fun getTMDBApiKey() {
    Properties props = new Properties()
    props.load(new FileInputStream(new File('secrets.properties')))
    return props['TMDB_API_KEY']
}

fun getOMDBApiKey() {
    Properties props = new Properties()
    props.load(new FileInputStream(new File('secrets.properties')))
    return props['OMDB_API_KEY']
}

fun getYoutubeApiKey() {
    Properties props = new Properties()
    props.load(new FileInputStream(new File('secrets.properties')))
    return props['YOUTUBE_API_KEY']
}*/

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //recyclerView
    val recyclerViewVersion = "1.3.2"
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")

    //fragment
    val fragmentVersion = "1.6.1"
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")

    //browser
    val browserVersion = "1.6.0"
    implementation("androidx.browser:browser:$browserVersion")

    val workVersion = "2.8.1"
    // Kotlin + coroutines
    implementation("androidx.work:work-runtime-ktx:$workVersion")

    val paleteVersion = "1.0.0"
    implementation("androidx.palette:palette-ktx:$paleteVersion")

    //Coroutines
    var coroutinesVersion = "1.7.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.concurrent:concurrent-futures-ktx:1.1.0")

    //retrofit
    var retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    //retrofit converters
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // Glide
    var glideVersion = "4.16.0"
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")

    // helps you build a cool carousel intro for your App
    implementation("com.github.AppIntro:AppIntro:6.3.1")

    //preferences
    val preferenceVersion = "1.2.1"
    implementation("androidx.preference:preference-ktx:$preferenceVersion")

    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    //lifecycle components
    val lifecycleVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    //navigation graph
    var navVersion = "2.6.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:29.0.4"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Hilt dependencies
    var hiltVersion = "2.48"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    //room DB
    val roomVersion = "2.5.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")

    //custom search view
    //implementation("com.github.ZinoKader:MaterialSearchView:1.4.2")
    //implementation("com.miguelcatalan:materialsearchview:1.4.0")
    implementation("com.github.Ferfalk:SimpleSearchView:0.2.0")

    //security
    //implementation("androidx.security:security-crypto:1.0.0")

    //to resolve class duplication of kotlin-stdlib-jdk7 and kotlin-stdlib-jdk8
    //implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))

    //ksp
    //implementation("com.google.devtools.ksp:symbol-processing-api:1.9.10-1.0.13")
}

kapt {
    correctErrorTypes = true
}