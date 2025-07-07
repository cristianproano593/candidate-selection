#!/bin/sh
set -e

echo "⏳ Esperando a MySQL en $DB_HOST:${DB_PORT:-3306}..."
until nc -z -w2 "$DB_HOST" "${DB_PORT:-3306}"; do sleep 2; done

echo "🚀 Arrancando Auth Service en puerto ${SERVER_PORT:-8090}..."
exec java -Dspring.profiles.active=prod \
          -Dserver.port=${SERVER_PORT:-8090} \
          -jar auth.jar
