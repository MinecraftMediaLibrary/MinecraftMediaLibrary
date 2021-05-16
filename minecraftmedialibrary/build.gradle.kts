import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

description = "minecraftmedialibrary"

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    api(project(":minecraftmedialibrary-api"))
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("uk.co.caprica:vlcj:4.7.1")
    compileOnly("uk.co.caprica:vlcj-natives:4.1.0")
    compileOnly("uk.co.caprica:native-streams:1.0.0")
    compileOnly("com.github.sealedtx:java-youtube-downloader:2.5.2")
    compileOnly("ws.schild:jave-core:3.0.1")
    compileOnly("io.netty:netty-all:5.0.0.Alpha2")
    compileOnly("com.mojang:authlib:1.5.25")
    compileOnly("org.jetbrains:annotations:20.1.0")
    compileOnly("org.ow2.asm:asm:9.1")
    compileOnly("org.ow2.asm:asm-commons:9.1")
    compileOnly("com.github.pulsebeat02:jarchivelib:master-SNAPSHOT")
    compileOnly("org.tukaani:xz:1.0")
    compileOnly("com.alibaba:fastjson:1.2.73")
    compileOnly("net.java.dev.jna:jna:5.7.0")
    compileOnly("org.bytedeco:javacv-platform:1.5.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testImplementation("io.github.glytching:junit-extensions:2.4.0")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.4.31")
    implementation(project(":v1_16_R3"))
    implementation(project(":v1_16_R2"))
    implementation(project(":v1_16_R1"))
    implementation(project(":v1_15_R1"))
    implementation(project(":v1_14_R1"))
    implementation(project(":v1_13_R2"))
    implementation(project(":v1_13_R1"))
    implementation(project(":v1_12_R1"))
    implementation(project(":v1_11_R1"))
    implementation(project(":v1_10_R1"))
    implementation(project(":v1_9_R2"))
    implementation(project(":v1_9_R1"))
    implementation(project(":v1_8_R3"))
    implementation(project(":v1_8_R2"))
    implementation(project(":v1_8_R1"))
}

tasks.withType<ShadowJar> {
    relocate("uk.co.caprica.vlcj", "com.github.pulsebeat02.vlcj")
    relocate("uk.co.caprica.vlcj.binding", "com.github.pulsebeat02.vlcj.binding")
    relocate("uk.co.caprica.nativestreams", "com.github.pulsebeat02.vlcj.nativestreams")
    relocate("com.github.kiulian.downloader", "com.github.pulsebeat02.youtube")
    relocate("ws.schild.jave", "com.github.pulsebeat02.jave")
    relocate("org.apache.commons.compress", "com.github.pulsebeat02.compress")
    relocate("org.rauschig.jarchivelib", "com.github.pulsebeat02.jarchivelib")
    relocate("org.tukaani.xz", "com.github.pulsebeat02.xz")
    relocate("org.ow2.asm", "com.github.pulsebeat02.asm")
    relocate("org.ow2.asm.commons", "com.github.pulsebeat02.asm.commons")
    relocate("org.apache.commons.io", "org.bukkit.craftbukkit.libs.org.apache.commons.io")
}
