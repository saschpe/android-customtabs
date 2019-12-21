plugins {
    kotlin("jvm") version "1.3.31"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

val ensureSecretsExist: Task by tasks.creating {
    val secretFile = File("buildSrc/src/main/kotlin/Secrets.kt")

    description = "Ensures that $secretFile exists"

    doFirst {
        if (!secretFile.exists()) {
            secretFile.writeText(
                """
object Secrets {
    object Bintray {
        const val user = ""
        const val apiKey = ""
    }
}""".trimIndent()
            )
        }
    }
}
tasks.getByName("assemble").dependsOn(ensureSecretsExist)
