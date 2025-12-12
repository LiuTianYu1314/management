// 导入你的组件
import { createRouter, createWebHistory } from 'vue-router';

import HomeView from '../views/HomeView.vue';
import DashboardLayout from '../components/DashboardLayout.vue';
import Users from '../views/charts/Users.vue'
import Student from "../views/charts/Student.vue";

const routes = [
    {
        path: '/',
        component: DashboardLayout,
        children: [
            {
                path: '',
                name: 'Home',
                component: HomeView,
            },
            // 左侧导航栏点击后的新页面路由
            {
                path: 'users',
                name: 'Users',
                component: Users,
                meta: { title: '学生信息' }
            },
            {
                path: 'student',
                name: 'Student',
                component: Student,
                meta: { title: '学生成绩' }
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