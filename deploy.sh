#!/usr/bin/env bash
set -euo pipefail

# Minimal deploy helper: build locally with --build, otherwise pull and up
cd "$(dirname "$0")/.." || exit 1

if [ "$#" -gt 0 ] && [ "$1" = "--build" ]; then
  docker compose -f docker-compose.prod.yml build --pull
else
  docker compose -f docker-compose.prod.yml pull || true
fi

docker compose -f docker-compose.prod.yml up -d --remove-orphans

echo "Services started. Use: docker compose -f docker-compose.prod.yml logs -f"
