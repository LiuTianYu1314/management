import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // 导入配置好的路由

const app = createApp(App);

app.use(router); // 注册路由

app.mount('#app');