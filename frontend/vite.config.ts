import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      // 所有 /api 开头的请求，都转发到你的 Spring Boot 后端
      '/api': {
        target: 'http://localhost:8080',   // ← 改成你后端实际运行的端口
        changeOrigin: true,
      }
    }
  }
})