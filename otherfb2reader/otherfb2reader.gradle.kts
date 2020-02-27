plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.3"

    defaultConfig {
        applicationId = "ru.chsergeig.android.otherfb2reader"
        minSdkVersion(15)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        signingConfigs {
//            named("debug").configure {
//                storeFile = file("debug.jks")
//                storePassword = "pass"
//                keyAlias = "keyAlias"
//                keyPassword = "otherPass"
//            }
//            register("release") {
//                storeFile = file("debug.jks")
//                storePassword = "pass"
//                keyAlias = "keyAlias"
//                keyPassword = "otherPass"
//            }
//        }
        buildTypes {
//            named("debug").configure {
//                applicationIdSuffix = ".debug"
//                isMinifyEnabled = false
//            }
//
//            named("release").configure {
//                isMinifyEnabled = true
//                signingConfig = signingConfigs.getByName("release")
//            }
        }
    }
}

dependencies {
    //    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.61")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.google.android.material:material:1.1.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
