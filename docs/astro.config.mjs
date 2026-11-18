// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
	site: 'https://jrudman25.github.io',
	base: '/PacModWorld2',
	integrations: [
		starlight({
			title: 'PacModWorld2',
			description:
				'A Minecraft mod that adds mobs, items, and movement abilities from Pac-Man World 2.',
			social: [
				{ icon: 'github', label: 'GitHub', href: 'https://github.com/jrudman25/PacModWorld2' },
			],
			customCss: ['./src/styles/custom.css'],
			editLink: {
				baseUrl: 'https://github.com/jrudman25/PacModWorld2/edit/main/docs/',
			},
			sidebar: [
				{
					label: 'Getting Started',
					items: [{ autogenerate: { directory: 'getting-started' } }],
				},
				{
					label: 'Features',
					items: [{ autogenerate: { directory: 'features' } }],
				},
				{ label: 'Changelog', slug: 'changelog' },
			],
		}),
	],
});
