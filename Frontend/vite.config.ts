import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import svgr from 'vite-plugin-svgr';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    svgr({
      svgrOptions: {
        icon: true,
      },
    }),
  ],
  resolve: {
    alias: [{ find: '@', replacement: '/src' }],
  },
  base: '/',
  server: {
    port: 3000,
    host: true,
  },
  // SockJS global 설정 필요해서 추가
  define: {
    global: 'window',
  },
});
