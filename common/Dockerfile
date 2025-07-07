# ====== Etapa de build con Maven preinstalado ======
FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /workspace

# 1) Copiar POM padre y todos los módulos
COPY pom.xml ./
COPY common           ./common
COPY auth-service     ./auth-service
COPY customer-service ./customer-service

# 2) Pre-descargar dependencias de todos los módulos
RUN mvn dependency:go-offline -B

# 3) Compilar solo common (sin tests) e instalarlo en el repositorio local
RUN mvn -pl common clean install -DskipTests -B

# ====== Etapa runtime ======
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 4) Copiar el JAR de common
COPY --from=builder /workspace/common/target/common-*.jar ./common.jar

# 5) Copiar y preparar el script de migraciones
COPY common/entrypoint-migrations.sh ./entrypoint.sh
RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
