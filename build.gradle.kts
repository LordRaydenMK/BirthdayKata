plugins {
    kotlin("jvm") version "1.3.61"
}

group = "com.sanastasov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")

    val arrow_version = "0.10.4"
    implementation("io.arrow-kt:arrow-core:$arrow_version")
    implementation("io.arrow-kt:arrow-syntax:$arrow_version")
    implementation("io.arrow-kt:arrow-fx:$arrow_version")

    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("io.github.microutils:kotlin-logging:1.7.7")
    implementation("org.slf4j:slf4j-simple:1.7.26")

    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2") {
        exclude("io.arrow-kt")
    }
    testImplementation("com.github.kirviq:dumbster:1.7.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}