plugins {
  id("org.springframework.boot") version "2.2.6.RELEASE"
  id("io.spring.dependency-management") version "1.0.9.RELEASE"
  java
}

group = "de.roamingthings"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val developmentOnly by configurations.creating

configurations {
  runtimeClasspath {
    extendsFrom(developmentOnly)
  }
}

repositories {
  mavenCentral()
}

extra["springCloudVersion"] = "Hoxton.SR3"
dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
  implementation("org.springframework.retry:spring-retry")
  runtimeOnly("org.springframework.boot:spring-boot-starter-aop")

  implementation(platform("org.testcontainers:testcontainers-bom:1.13.0"))
  implementation("org.testcontainers:testcontainers")
  runtimeOnly("junit:junit:4.13")

  implementation("io.github.resilience4j:resilience4j-micrometer")

  implementation("io.micrometer:micrometer-registry-prometheus")

  developmentOnly("org.springframework.boot:spring-boot-devtools")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }

  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

  testCompileOnly("org.projectlombok:lombok")
  testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
