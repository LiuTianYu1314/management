// 导入你的组件
import { createRouter, createWebHistory } from 'vue-router';

import HomeView from '../views/HomeView.vue';
import DashboardLayout from '../components/DashboardLayout.vue';
import BasicLineChart from '../views/charts/Users.vue';

const routes = [
    {
        path: '/',
        // 使用 DashboardLayout 作为顶级路由组件
        component: DashboardLayout,
        children: [
            {
                path: '', // 默认子路由，对应 /
                name: 'Home',
                component: HomeView,
            },
            // 左侧导航栏点击后的新页面路由
            {
                path: 'basic-line-chart', // 对应点击 "折线图" -> "基础折线图"
                name: 'BasicLineChart',
                component: BasicLineChart,
                meta: { title: '基础折线图' }
            },
            // ... 其他图表组件的路由
        ]
    },
    // ... 其他非布局内的路由
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;