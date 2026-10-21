# syntax=docker/dockerfile:1

# ---------- Build stage ----------
FROM gradle:9.5.1-jdk25 AS build
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
RUN ./gradlew clean bootWar --no-daemon -x test \
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
