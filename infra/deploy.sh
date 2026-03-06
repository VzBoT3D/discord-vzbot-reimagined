#!/usr/bin/env bash
set -euo pipefail

ENV="${1:?Usage: deploy.sh <stage|production>}"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

case "$ENV" in
  stage)
    export BACKEND_TAG="stage-latest"
    export FRONTEND_TAG="stage-latest"
    ;;
  production)
    export BACKEND_TAG="latest"
    export FRONTEND_TAG="latest"
    ;;
  *)
    echo "Unknown environment: $ENV"
    echo "Usage: deploy.sh <stage|production>"
    exit 1
    ;;
esac

echo "Deploying $ENV environment..."
echo "  Backend tag:  $BACKEND_TAG"
echo "  Frontend tag: $FRONTEND_TAG"

docker compose -f "$SCRIPT_DIR/docker-compose.yml" pull bot frontend
docker compose -f "$SCRIPT_DIR/docker-compose.yml" up -d

echo "Deployment complete."
