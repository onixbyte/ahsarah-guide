plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

val artefactVersion: String by project
val buildChannel: String by project
val vendor: String by project

group = "com.onixbyte.ahsarahguide"
version = artefactVersion

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// Generate META-INF/build-info.properties at build time. The build version comes
// from the project version (driven by the git tag in CI); channel and vendor are
// stamped alongside it so the runtime no longer needs template placeholders.
springBoot {
    buildInfo {
        properties {
            additional.set(
                mapOf(
                    "channel" to buildChannel,
                    "vendor" to vendor
                )
            )
        }
    }
}

dependencies {
    // BOMs are imported as platforms, so they cannot be bundled.
    implementation(platform(libs.aws.sdk.bom))
    implementation(platform(libs.onixbyte.versionCatalogue))

    implementation(libs.bundles.utilities)
    implementation(libs.bundles.onixbyte)
    implementation(libs.bundles.web)
    implementation(libs.bundles.persistence)
    implementation(libs.bundles.security)
    implementation(libs.bundles.cache)
    implementation(libs.bundles.observability)
    implementation(libs.bundles.mail)
    implementation(libs.bundles.aws)
    implementation(libs.bundles.docs)

    // Annotation processor for @ConfigurationProperties; kept out of bundles.
    implementation(libs.spring.boot.configurationProcessor)

    runtimeOnly(libs.postgres.driver)

    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.bundles.testRuntime)
}

tasks.test {
    useJUnitPlatform()
}
