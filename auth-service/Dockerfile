FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copiar POM padre y módulos
COPY pom.xml ./
COPY common           ./common
COPY auth-service     ./auth-service
COPY customer-service ./customer-service

# Compilar common + auth-service juntos (-am arma dependencias)
RUN mvn -B -pl auth-service -am clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /workspace/auth-service/target/auth-service-*.jar ./auth.jar
COPY auth-service/entrypoint-auth.sh ./entrypoint.sh
RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
