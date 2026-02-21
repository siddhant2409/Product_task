# Stage 1: Build
FROM maven:3.9.2-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy pom.xml
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Product Manager Team"
LABEL description="Product Manager API - RESTful API for Product Management"

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /build/target/product-manager-api-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Set environment variables
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Run the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
