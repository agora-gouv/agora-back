import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
}

group = "fr.social.gouv.agora"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("redis.clients:jedis")
    implementation("com.google.firebase:firebase-admin:9.1.1")
    implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20220608.1")
    implementation("org.deeplearning4j:deeplearning4j-core:1.0.0-M2.1") {
        exclude(group = "org.bytedeco", module = "opencv")
        exclude(group = "org.bytedeco", module = "opencv-platform")
        exclude(group = "org.bytedeco", module = "ffmpeg")
        exclude(group = "org.bytedeco", module = "fmpeg-platform")
    }
    implementation("org.deeplearning4j:deeplearning4j-nlp:1.0.0-M2.1")
    implementation("org.nd4j:nd4j-native:1.0.0-M2.1:linux-x86_64")
    implementation("org.nd4j:nd4j-native-platform:1.0.0-M2.1")
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.4")
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.4:models-french")
    implementation("org.codehaus.plexus:plexus-archiver:4.8.0")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("bootJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}