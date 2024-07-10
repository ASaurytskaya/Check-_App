plugins {
    id("java")
    id("jacoco")
    application
}

group = "ru.clevertec"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.postgresql:postgresql:42.2.18")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.mockito:mockito-core:4.0.0")

    implementation ("org.jacoco:org.jacoco.core:0.8.7")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "ru.clevertec.check.CheckRunner"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "ru.clevertec.check.CheckRunner"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
}

