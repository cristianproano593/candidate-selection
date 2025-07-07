#!/bin/sh
set -e

echo "⏳ Esperando a Auth Service en $AUTH_HOST:${AUTH_PORT:-8090}..."
until nc -z -w2 "${AUTH_HOST:-auth-service}" "${AUTH_PORT:-8090}"; do sleep 2; done

echo "🚀 Arrancando Customer Service en puerto ${SERVER_PORT:-8091}..."
exec java -Dspring.profiles.active=prod \
          -Dserver.port=${SERVER_PORT:-8091} \
          -jar customer.jar
