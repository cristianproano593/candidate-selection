@echo off
setlocal enabledelayedexpansion

rem --- Generar Dockerfile ---
> Dockerfile (
  echo # === Etapa de build ===
  echo FROM eclipse-temurin:17-jdk-alpine AS builder
  echo WORKDIR /workspace
  echo COPY . .
  echo RUN mvnw -q -DskipTests clean package ^
      -pl common,auth-service,customer-service -am
  echo(
  echo # === Etapa runtime ===
  echo FROM eclipse-temurin:17-jre-alpine
  echo RUN addgroup -S spring && adduser -S spring -G spring
  echo USER spring:spring
  echo(
  echo COPY --from=builder /workspace/common/target/common-*.jar          /common.jar
  echo COPY --from=builder /workspace/auth-service/target/auth-service-*.jar      /auth.jar
  echo COPY --from=builder /workspace/customer-service/target/customer-service-*.jar /customer.jar
  echo(
  echo COPY entrypoint.sh /entrypoint.sh
  echo RUN chmod +x /entrypoint.sh
  echo(
  echo EXPOSE 8080 8081
  echo ENTRYPOINT ["/entrypoint.sh"]
)

rem --- Generar entrypoint.sh ---
> entrypoint.sh (
  echo #!/bin/sh
  echo set -e
  echo(
  echo echo %^"⏳ Esperando a MySQL en $DB_HOST...%^"
  echo until nc -z -w 2 "$DB_HOST" "${DB_PORT:-3306}"; do sleep 2; done
  echo echo %^"✅ MySQL disponible, ejecutando migraciones (common)…%^"
  echo java -Dspring.profiles.active=prod -jar /common.jar
  echo(
  echo echo %^"🚀 Arrancando auth-service (8080)…%^"
  echo java -Dspring.profiles.active=prod -jar /auth.jar ^
      ^
  echo(
  echo echo %^"🚀 Arrancando customer-service (8081)…%^"
  echo java -Dspring.profiles.active=prod -Dserver.port=8081 -jar /customer.jar ^
      ^
  echo(
  echo wait -n
)

rem --- Dar permisos de ejecución en WSL (opcional) ---
echo Para dar permisos ejecutables a entrypoint.sh, si usas WSL, ejecuta:
echo     chmod +x entrypoint.sh

echo.
echo Dockerfile y entrypoint.sh generados.
endlocal
