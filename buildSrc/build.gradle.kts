plugins {
    kotlin("jvm") version "1.3.61"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks {
    val ensureSecretsExist by registering {
        val secretFile = File("$projectDir/src/main/kotlin/Secrets.kt")
        description = "Ensures that '$secretFile' exists"

        outputs.file(secretFile)
        doFirst {
            if (!secretFile.exists()) {
                secretFile.writeText(
                    """
object Secrets {
    object Bintray {
        const val user = ""
        const val apiKey = ""
    }
}

""".trimIndent()
                )
            }
        }
    }
    named("assemble") { dependsOn(ensureSecretsExist) }
}
