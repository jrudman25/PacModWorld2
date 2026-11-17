# AGENTS.md

Guidance for coding agents working on PacModWorld2, a Minecraft mod adding
mobs, items, and movement abilities from Pac-Man World 2 (NeoForge 1.21.1).

## Repository layout

- `neoforge-1.21.1/src/main/` — mod source (Java) and resources
- `neoforge-1.21.1/src/generated/` — generated resources (data generators)
- `art/` — Blockbench source models (`.bbmodel`)
- `docs/` — documentation site, a separate Astro + Starlight npm project
  (see `docs/AGENTS.md`); not part of the Gradle build
- `.github/workflows/` — `build.yml` builds and game-tests the mod; `docs.yml`
  deploys the docs site to GitHub Pages

## Build and test (mod)

Requires JDK 21. On Windows, `gradlew.bat` uses `JAVA21_HOME` if it is set.

- `./gradlew build` builds `build/libs/pacmodworld2-neoforge-1.21.1-<version>.jar`
- `./gradlew runClient` launches a development client
- `./gradlew runGameTestServer` runs the automated game tests; CI fails the
  build on `pacmodworld2` ERROR/FATAL lines in
  `runs/gameTestServer/logs/latest.log`

## Docs site (`docs/`)

- `npm ci`, then `npm run dev` — dev server at
  `http://localhost:4321/PacModWorld2`. The site is served under the
  `/PacModWorld2` base path; internal links include it.
- `npm run build` writes static output to `docs/dist/`.
- Dependencies in `docs/package.json` are pinned: the npm registry proxy
  enforces a minimum release age of about 7 days. Bump versions deliberately,
  after they have been published long enough.
- `docs.yml` deploys on pushes to `main` that touch `docs/**` only, so
  mod-only changes do not trigger a docs deploy.

## Conventions

- Agents do not commit, push, or create branches. Leave changes uncommitted
  for the owner to review and commit.
- The docs site is the wiki: when mod features change, update the relevant
  page under `docs/src/content/docs/` and the changelog. Keep the root
  `README.md` a short overview; do not duplicate full documentation there.
