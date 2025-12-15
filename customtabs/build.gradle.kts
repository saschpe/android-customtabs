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
    id("com.android.library")
    kotlin("android")
    `maven-publish`
    signing
}

dependencies {
    api("androidx.browser:browser:1.8.0")

    implementation("androidx.appcompat:appcompat:1.7.0")

    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation("org.robolectric:robolectric:4.16")
    testImplementation("org.mockito:mockito-core:5.21.0")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

android {
    namespace = "saschpe.android.customtabs"

    defaultConfig {
        compileSdk = 34
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenAndroid") {
            artifactId = "android-customtabs"
            groupId = "de.peilicke.sascha"
            version = "3.1.1"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Android CustomTabs")
                description.set("Chrome CustomTabs for Android demystified. Simplifies development and provides higher level classes including fallback in case Chrome isn't available on device.")
                url.set("https://github.com/saschpe/android-customtabs")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("saschpe")
                        name.set("Sascha Peilicke")
                        email.set("sascha@peilicke.de")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/saschpe/android-customtabs.git")
                    developerConnection.set("scm:git:ssh://github.com/saschpe/android-customtabs.git")
                    url.set("https://github.com/saschpe/android-customtabs")
                }
            }
        }
    }

    if (hasProperty("sonatypeUser") && hasProperty("sonatypePass")) {
        repositories {
            maven {
                name = "sonatype"
                credentials {
                    username = property("sonatypeUser") as String
                    password = property("sonatypePass") as String
                }
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenAndroid"])
}
