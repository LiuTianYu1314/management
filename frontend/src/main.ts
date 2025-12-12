// main.js（或 main.ts）
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 关键：引入 Ant Design Vue 3
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'  // 新版推荐使用 reset.css

const app = createApp(App)

app.use(router)
app.use(Antd)   // 全局注册所有组件

app.mount('#app')