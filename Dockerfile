# syntax=docker/dockerfile:1

# ---------- Build stage ----------
# BUILDPLATFORM keeps this stage running natively on the builder machine, so
# the (platform independent) WAR is compiled once and reused for every target
# platform in a multi-arch build instead of being emulated under QEMU.
FROM --platform=$BUILDPLATFORM gradle:9.5.1-jdk25 AS build
WORKDIR /app

# Copy only build descriptors first to leverage Docker layer caching
COPY settings.gradle.kts build.gradle.kts gradle.properties* ./
COPY gradle ./gradle
COPY gradlew ./gradlew
RUN chmod +x ./gradlew

# Copy the rest of the source
COPY . .

# Build the executable WAR (skip tests; run them in CI separately)
# Normalise the artifact name so the runtime stage never depends on the
# Gradle project name / version (rootProject.name in settings.gradle.kts).
# The cache mount keeps the Gradle distribution and dependency cache between
# builds on the same builder. Note that BuildKit does NOT export cache mounts
# via --cache-to, so on CI (where every run starts with a fresh builder) it
# begins empty; it mainly speeds up local rebuilds.
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew clean bootWar --no-daemon -x test \
    && cp build/libs/*.war /app/app.war

# ---------- Runtime stage ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the built WAR (staged under a stable name by the build stage)
COPY --from=build /app/app.war /app/app.war

# Persistent data directory for the SQLite database
RUN mkdir -p /data
VOLUME ["/data"]

# SQLite DB location (absolute path so it lands on the volume)
ENV DB_URL=jdbc:sqlite:/data/springdb.db
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.war"]
