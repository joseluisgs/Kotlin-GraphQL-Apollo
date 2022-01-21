import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
    // Apollo 3.0.0
    id("com.apollographql.apollo3").version("3.0.0")

}

group = "es.joseluisgs.dam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Apollo 3.0.0
    implementation("com.apollographql.apollo3:apollo-runtime:3.0.0")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.8.9")
    // https://mvnrepository.com/artifact/com.diogonunes/JColor
    implementation("com.diogonunes:JColor:5.2.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}

// Le indicamos a Apollo que genere en Kotlin los modelos POKO
apollo {
    // Donde se encuentran los modelos POKO
    packageName.set("graphql.com.models")
    // Si queremos que genere datos falsos para las pruebas
    generateTestBuilders.set(true)
}