---
title: Installation
description: How to download and install PacModWorld2 for Minecraft 1.21.1 on NeoForge.
---

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.219 or later
- [GeckoLib](https://modrinth.com/mod/geckolib) 4.9.3 or later for 1.21.1

## Download

Download **one** JAR for the Minecraft installation you use. Builds are
available as artifacts on the repository's
[GitHub Actions runs](https://github.com/jrudman25/PacModWorld2/actions).

## Install

Install the mod and GeckoLib on every client. For multiplayer, install both on
the server as well.

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
