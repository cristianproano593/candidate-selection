#!/bin/sh
set -e

echo "⏳ Esperando a MySQL en $DB_HOST:${DB_PORT:-3306}..."
until nc -z -w2 "$DB_HOST" "${DB_PORT:-3306}"; do sleep 2; done

echo "✅ Ejecutando migraciones (common)…"
java -Dspring.profiles.active=prod -jar common.jar

echo "🔄 Migraciones completadas. Saliendo."
