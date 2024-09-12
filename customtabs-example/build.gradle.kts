/*
 * Copyright 2017 Sascha Peilicke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(project(":customtabs"))
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.vectordrawable:vectordrawable-animated:1.2.0")
    implementation("com.google.android.material:material:1.12.0")
}

android {
    namespace = "com.example.saschpe.customtabs"

    defaultConfig {
        applicationId = "com.example.saschpe.customtabs"
        compileSdk = 34
        minSdk = 21
        targetSdk = 34
        versionCode = 210030003
        versionName = "3.0.3"
        base.archivesName = "$applicationId-$versionName"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions.jvmTarget = "17"
}
