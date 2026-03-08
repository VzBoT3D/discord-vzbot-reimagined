# VzBot Discord Bot & Website

A Discord bot and companion website for the [VzBot](https://vzbot.org) 3D printer community. Manages serial number assignments, printer profiles, community statistics, and blog content.

## Features

- **Serial Number Management** - Apply for, review, and assign serial numbers through Discord with auto-generated STL badge files
- **Printer Profiles** - Catalog of VzBot printer variants with media and specifications
- **Community Map** - Interactive 3D globe showing VzBot builds worldwide
- **Blog System** - Community posts and announcements
- **Analytics** - Registration trends and printer distribution stats

## Tech Stack

| Component | Technology |
|-----------|------------|
| Backend | Kotlin, Ktor, Exposed ORM |
| Frontend | Nuxt 4, Vue 3, Tailwind CSS |
| Database | MariaDB 11.5 |
| Discord | JDA 5, KtBot |
| Deployment | Docker, GitHub Container Registry |

## Project Structure

```
backend/     Kotlin/Ktor Discord bot and REST API
frontend/    Nuxt 4 website
```

## Setup

### Prerequisites

- Docker & Docker Compose
- (For local dev) Java 21, Bun

### Quick Start

1. Copy the environment template and fill in your values:
   ```sh
   cp .env.template .env
   ```

2. Configure required environment variables in `.env`:
   - `VZ_TOKEN` - Discord bot token
   - `VZ_ADMIN_ROLE` / `VZ_TEAM_ROLE` - Discord role IDs
   - `VZ_SERIAL_CATEGORY` - Discord category for serial ticket channels
   - `VZ_SERIAL_ANNOUNCEMENT_CHANNEL` - Channel for serial announcements
   - `VZ_SERIAL_BASE_PLATE_HOST_PATH` / `VZ_SERIAL_NUMBER_PLATES_HOST_PATH` - Host paths to STL files
   - `BACKEND_TOKEN` - Shared token for frontend-to-backend auth

3. Start the stack:
   ```sh
   docker compose up -d
   ```

The frontend will be available on port **3000**. The backend API runs on port **8080** (internal only by default).

### Local Development

**Backend:**
```sh
cd backend
./gradlew run
```

**Frontend:**
```sh
cd frontend
bun install
bun run dev
```
