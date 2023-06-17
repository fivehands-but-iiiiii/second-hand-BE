import react from '@vitejs/plugin-react';
import { defineConfig } from 'vite';
import svgr from 'vite-plugin-svgr';

export default defineConfig({
  plugins: [react(), svgr()],
  resolve: {
    alias: [
      { find: '@pages', replacement: '/src/pages' },
      { find: '@common', replacement: '/src/components/common' },
      { find: '@components', replacement: '/src/components' },
      { find: '@constants', replacement: '/src/constants' },
      { find: '@assets', replacement: '/src/assets' },
      { find: '@styles', replacement: '/src/styles' },
      { find: '@routes', replacement: '/src/routes' },
      { find: '@utils', replacement: '/src/utils' },
      {
        find: '@',
        replacement: '/src',
      },
    ],
  },
});
