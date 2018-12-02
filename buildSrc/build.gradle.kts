import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.10"
}

repositories {
    jcenter()
}

val ensureSecretsExist by tasks.creating {
    val secretFile = File("buildSrc/src/main/kotlin/Secrets.kt")

    description = "Ensures that $secretFile exists"

    doFirst {
        if (!secretFile.exists()) {
            secretFile.writeText("object Secrets {\n" +
                    "    object Bintray {\n" +
                    "        val user = \"\"\n" +
                    "        val apiKey = \"\"\n" +
                    "    }\n" +
                    "}")
        }
    }
}
tasks.getByName("assemble").dependsOn(ensureSecretsExist)

dependencies {
    compile(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}