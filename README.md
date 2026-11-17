# PacModWorld2

PacModWorld2 is a Minecraft mod that adds mobs, items, and movement abilities from Pac-Man World 2.

All Pac-Man World 2 IP belongs to the original copyright owners. Assets for this mod were created from scratch.

## Status

In development (0.1.0, not yet released). Currently targets Minecraft 1.21.1 on NeoForge.

## Features

- **B-Doing**: a 3x3 bounce pad. Landing on it launches players and mobs about 6 blocks into the air, and landing on it never causes fall damage. Sneak to stand on it without bouncing.
  - Recipe: slime blocks in the corners, iron ingots on the edges, and a piston in the center.
- **Red Rat**: a small hostile mob that hops around grassy biomes (plains, meadows, forests, savannas) at any time of day. It attacks with a short-range flame breath that sets targets on fire but never burns blocks. It is immune to fire and drops fire charges.

All items are in the PacModWorld2 creative tab, including a Red Rat spawn egg.

## Download and install

Requirements:

- Minecraft 1.21.1
- NeoForge 21.1.219 or later
- [GeckoLib](https://modrinth.com/mod/geckolib) 4.9.3 or later for 1.21.1

Download **one** JAR for the Minecraft installation you use. Builds are available as artifacts on the repository's GitHub Actions runs.

Install the mod and GeckoLib on every client. For multiplayer, install both on the server as well.

## Building from source

Requires JDK 21. On Windows, `gradlew.bat` uses `JAVA21_HOME` if it is set.

- `./gradlew build` builds `build/libs/pacmodworld2-neoforge-1.21.1-<version>.jar`
- `./gradlew runClient` launches a development client
- `./gradlew runGameTestServer` runs the automated game tests

## Documentation

The documentation site is at <https://jrudman25.github.io/PacModWorld2/>. Its
source lives in `docs/` (Astro + Starlight) and deploys to GitHub Pages on
pushes to `main` that touch `docs/`.

## License

[MIT](LICENSE)
