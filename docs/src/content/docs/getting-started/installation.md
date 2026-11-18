---
title: Installation
description: How to download and install PacModWorld2 for Minecraft 1.21.1 on NeoForge.
---

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.219 or later
- [GeckoLib](https://modrinth.com/mod/geckolib) 4.9.3 or later for 1.21.1

## Download

There is no published release yet. Development builds are available as
artifacts on the repository's
[GitHub Actions runs](https://github.com/jrudman25/PacModWorld2/actions):

1. Open a recent successful run of **Build download artifacts** on the `main`
   branch. Downloading artifacts requires being signed in to GitHub.
2. Download the `pacmodworld2-neoforge-1.21.1` artifact. GitHub delivers it as
   a ZIP file.
3. Extract `pacmodworld2-neoforge-1.21.1-<version>.jar` from the ZIP.

## Install

1. Install NeoForge 21.1.219 or later for Minecraft 1.21.1.
2. Put the PacModWorld2 JAR and the GeckoLib JAR in the `mods` folder of that
   installation.
3. Launch the game with the NeoForge profile.

Install both mods on every client. For multiplayer, install both on the server
as well.

## Building from source

Requires JDK 21. On Windows, `gradlew.bat` uses `JAVA21_HOME` if it is set.

```sh
./gradlew build
```

This produces `build/libs/pacmodworld2-neoforge-1.21.1-<version>.jar`.

Other useful tasks:

```sh
./gradlew runClient           # launches a development client
./gradlew runGameTestServer   # runs the automated game tests
```
