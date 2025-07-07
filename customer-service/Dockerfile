FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copiar POM padre y módulos
COPY pom.xml ./
COPY common           ./common
COPY auth-service     ./auth-service
COPY customer-service ./customer-service


# Compilar common + customer-service juntos (-am arma dependencias)
RUN mvn -B -pl customer-service -am clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /workspace/customer-service/target/customer-service-*.jar ./customer.jar
COPY customer-service/entrypoint-customer.sh ./entrypoint.sh
RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
