plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
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
    object Sonatype {
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
