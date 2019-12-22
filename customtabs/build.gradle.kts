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
    id("org.jetbrains.dokka") version "0.10.0"
    `maven-publish`
}

repositories {
    google()
    jcenter()
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(29)
        versionName = "3.0.1"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = "1.8"

    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api("androidx.browser:browser:1.2.0")

    implementation(kotlin("stdlib-jdk8", "1.3.61"))
    implementation("androidx.appcompat:appcompat:1.1.0")

    testImplementation("androidx.test:core:1.2.0")
    testImplementation("androidx.test.ext:junit:1.1.1")
    testImplementation("org.robolectric:robolectric:4.3.1") {
        // https://github.com/robolectric/robolectric/issues/4621
        exclude(group = "com.google.auto.service", module = "auto-service")
    }
    testImplementation("org.mockito:mockito-core:2.27.0")
}

group = "saschpe.android"
version = android.defaultConfig.versionName.toString()

tasks {
    val dokkaJavadoc by creating(org.jetbrains.dokka.gradle.DokkaTask::class) {
        outputFormat = "javadoc"
        outputDirectory = "$buildDir/javadoc"
        configuration {
            sourceLink {
                path = "src/main/java"
                url = "https://github.com/saschpe/android-customtabs/tree/master/customtabs/src/main/java"
                lineSuffix = "#L"
            }
        }
    }

    register("androidJavadocJar", Jar::class) {
        archiveClassifier.set("javadoc")
        from("$buildDir/javadoc")
        dependsOn(dokkaJavadoc)
    }

    register("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenAndroid") {
            artifactId = "customtabs"

            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
            artifact(tasks.getByName("androidJavadocJar"))
            artifact(tasks.getByName("androidSourcesJar"))

            pom {
                name.set("Android CustomTabs")
                description.set("Chrome CustomTabs for Android demystified. Simplifies development and provides higher level classes including fallback in case Chrome isn't available on device.")
                url.set("https://github.com/saschpe/android-customtabs")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
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

                withXml {
                    fun groovy.util.Node.addDependency(dependency: Dependency, scope: String) {
                        appendNode("dependency").apply {
                            appendNode("groupId", dependency.group)
                            appendNode("artifactId", dependency.name)
                            appendNode("version", dependency.version)
                            appendNode("scope", scope)
                        }
                    }

                    asNode().appendNode("dependencies").let { dependencies ->
                        // List all "api" dependencies as "compile" dependencies
                        configurations.api.get().allDependencies.forEach {
                            dependencies.addDependency(it, "compile")
                        }
                        // List all "implementation" dependencies as "runtime" dependencies
                        configurations.implementation.get().allDependencies.forEach {
                            dependencies.addDependency(it, "runtime")
                        }
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "bintray"
            credentials {
                username = Secrets.Bintray.user
                password = Secrets.Bintray.apiKey
            }
            url = uri("https://api.bintray.com/maven/saschpe/maven/android-customtabs/;publish=1")
        }
    }
}
