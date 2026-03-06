FROM oven/bun:1 AS build

WORKDIR /app
COPY package.json bun.lock ./
RUN bun install --frozen-lockfile

COPY . .
RUN bun run build

FROM node:20-slim AS runner

ENV NODE_ENV=production
EXPOSE 3000

WORKDIR /app

COPY --from=build /app/.output .output

ENTRYPOINT ["node", ".output/server/index.mjs"]
