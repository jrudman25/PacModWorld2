# PacModWorld2 documentation site

Astro + Starlight source for <https://jrudman25.github.io/PacModWorld2/>.

## Commands

Run from this directory:

| Command           | Action                                               |
| :---------------- | :--------------------------------------------------- |
| `npm ci`          | Install dependencies                                 |
| `npm run dev`     | Dev server at `http://localhost:4321/PacModWorld2`   |
| `npm run build`   | Build the production site to `dist/`                 |
| `npm run preview` | Preview the production build locally                 |

Content lives in `src/content/docs/` — one `.md`/`.mdx` file per page. Sidebar
navigation is configured in `astro.config.mjs`. The site deploys to GitHub
Pages via `.github/workflows/docs.yml`.
