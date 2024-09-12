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
    id("org.jetbrains.dokka") version "1.4.32"
    `maven-publish`
    signing
}

repositories {
    google()
    mavenCentral()
}

android {
    namespace = "saschpe.android.customtabs"

    defaultConfig {
        compileSdk = 31
        minSdk = 16
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
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

    implementation("androidx.appcompat:appcompat:1.2.0")

    testImplementation("androidx.test:core:1.3.0")
    testImplementation("androidx.test.ext:junit:1.1.2")
    testImplementation("org.robolectric:robolectric:4.7.1")
    testImplementation("org.mockito:mockito-core:3.9.0")
}

group = "de.peilicke.sascha"
version = android.defaultConfig.versionName.toString()

tasks {
    register("javadocJar", Jar::class) {
        dependsOn(named("dokkaHtml"))
        archiveClassifier.set("javadoc")
        from("$buildDir/dokka/html")
    }

    register("sourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenAndroid") {
            artifactId = "android-customtabs"
            version = "3.0.3"

            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
            artifact(tasks.getByName("javadocJar"))
            artifact(tasks.getByName("sourcesJar"))

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
            name = "sonatype"
            credentials {
                username = Secrets.Sonatype.user
                password = Secrets.Sonatype.apiKey
            }
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenAndroid"])
}
