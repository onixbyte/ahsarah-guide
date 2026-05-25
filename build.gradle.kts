plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

val artefactVersion: String by project

group = "com.onixbyte.helix"
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

dependencies {
    implementation(libs.jspecify.core)
    implementation(platform(libs.aws.sdk.bom))
    implementation(libs.aws.sdk.s3)
    implementation(libs.commons.io)
    implementation(libs.commons.lang)
    implementation(libs.commons.collections)
    implementation(libs.hypersistence.core)
    implementation(platform(libs.onixbyte.versionCatalogue))
    implementation(libs.onixbyte.tuple)
    implementation(libs.onixbyte.commonToolbox)
    implementation(libs.onixbyte.mathToolbox)
    implementation(libs.onixbyte.identityGenerator)
    implementation(libs.onixbyte.captcha)
    implementation(libs.onixbyte.regions)
    implementation(libs.jwt.core)
    implementation(libs.spring.boot.configurationProcessor)
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.webFlux)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.redis)
    implementation(libs.spring.boot.starter.cache)
    implementation(libs.spring.boot.starter.jpa)
    implementation(libs.mybatis.starter.core)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)
    implementation(libs.jackson.jsr310)
    implementation(libs.spring.boot.starter.doc)
    implementation(libs.spring.boot.starter.security)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.reactor.test)
    testImplementation(libs.mybatis.starter.test)
    testImplementation(libs.spring.security.test)
    runtimeOnly(libs.postgres.driver)
    testRuntimeOnly(libs.h2.database)
    testRuntimeOnly(libs.junit.launcher)
}

tasks.test {
    useJUnitPlatform()
}
