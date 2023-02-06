import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.compose") version "1.3.0"
    kotlin("plugin.serialization") version "1.8.0"
}

val projGroupId: String by project
val projArtifactId: String by project
val projName: String by project
val projVersion: String by project
val projDesc: String by project
val projVcs: String by project
val projBranch: String by project
val orgName: String by project
val orgUrl: String by project
val developers: String by project

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    // temporary maven repositories
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/releases") }
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    compileOnly("org.jetbrains:annotations:23.1.0")
    testCompileOnly("org.jetbrains:annotations:23.1.0")
}

compose.desktop {
    application {
        mainClass = "org.overrun.vmdw.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
        }
    }
}

val targetJavaVersion = 17
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    withJavadocJar()
    withSourcesJar()
}

tasks.named<Javadoc>("javadoc") {
    isFailOnError = false
    options {
        encoding = "UTF-8"
        locale = "en_US"
        windowTitle = "$projName $projVersion Javadoc"
        if (this is StandardJavadocDocletOptions) {
            charSet = "UTF-8"
            isAuthor = true
            links("https://docs.oracle.com/en/java/javase/17/docs/api/")
        }
    }
}

kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.named<Jar>("jar") {
    manifestContentCharset = "utf-8"
    setMetadataCharset("utf-8")
    from("LICENSE")
    manifest.attributes(
        "Specification-Title" to archiveBaseName,
        "Specification-Vendor" to orgName,
        "Specification-Version" to "0",
        "Implementation-Title" to archiveBaseName,
        "Implementation-Vendor" to orgName,
        "Implementation-Version" to archiveVersion
    )
}

tasks.named<Jar>("sourcesJar") {
    dependsOn(tasks.named("classes"))
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource, "LICENSE")
}

tasks.named<Jar>("javadocJar") {
    val javadoc by tasks
    dependsOn(javadoc)
    archiveClassifier.set("javadoc")
    from(javadoc, "LICENSE")
}

tasks.withType<Jar> {
    archiveBaseName.set(projArtifactId)
}

artifacts {
    archives(tasks.named("javadocJar"))
    archives(tasks.named("sourcesJar"))
}
