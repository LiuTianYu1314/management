<!-- DashboardLayout.vue —— 支持点击收起侧边栏（带动画 + 图标自动切换） -->
<template>
  <div class="dashboard-container">
    <!-- 侧边栏 -->
    <aside :class="['sidebar', { collapsed: isCollapsed }]">
      <div class="logo">
        <span v-if="!isCollapsed">Magic Admin</span>
        <span v-else>MA</span>
      </div>

      <nav class="menu">
        <router-link :to="{ name: 'Home' }" class="menu-item" exact-active-class="active">
          <i class="menu-icon home"></i>
          <span class="menu-text">首页</span>
        </router-link>
        <router-link :to="{ name: 'Users' }" class="menu-item" active-class="active">
          <i class="menu-icon users"></i>
          <span class="menu-text">学生信息</span>
        </router-link>
        <router-link :to="{ name: 'Student' }" class="menu-item" active-class="active">
          <i class="menu-icon score"></i>
          <span class="menu-text">学生成绩</span>
        </router-link>
      </nav>

      <!-- 收起/展开按钮 -->
      <div class="collapse-btn" @click="isCollapsed = !isCollapsed">
        <double-right-outlined v-if="isCollapsed" />
        <double-left-outlined v-else />
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="content-area">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  DoubleRightOutlined,
  DoubleLeftOutlined
} from '@ant-design/icons-vue'

// 控制侧边栏展开/收起
const isCollapsed = ref(false)
</script>

<style scoped>
.dashboard-container {
  display: flex;
  height: 100vh;
  background-color: #f0f2f5;
  overflow: hidden;
}

.sidebar {
  width: 200px;
  background-color: #001529;
  color: white;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

/* 收起状态 */
.sidebar.collapsed {
  width: 64px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  padding: 20px 0;
  white-space: nowrap;
  overflow: hidden;
  transition: all 0.3s;
}

.menu {
  flex: 1;
  padding: 10px 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #a6adb4;
  text-decoration: none;
  transition: all 0.3s;
  white-space: nowrap;
  overflow: hidden;
}

.menu-item:hover,
.menu-item.active {
  background-color: #1890ff;
  color: white;
}

/* 小图标（用伪元素简单画，也可以用 iconfont） */
.menu-icon {
  width: 16px;
  height: 16px;
  margin-right: 12px;
  display: inline-block;
  background: currentColor;
  -webkit-mask-size: contain;
  mask-size: contain;
  -webkit-mask-repeat: no-repeat;
  mask-repeat: no-repeat;
}

.home   { -webkit-mask-image: url('data:image/svg+xml;utf8,<svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path d="M946.5 505L560.1 118.8l-25.9-25.9c-12.3-12.3-32.2-12.3-44.6 0L56.5 505c-12.3 12.3-12.3 32.2 0 44.6 12.3 12.3 32.2 12.3 44.6 0l35.7-35.7V790c0 17.7 14.3 32 32 32h192v-192h96v192h192c17.7 0 32-14.3 32-32V514.3l35.7 35.7c12.3 12.3 32.2 12.3 44.6 0 12.3-12.3 12.3-32.2 0-44.6z"/></svg>'); }
.users  { -webkit-mask-image: url('data:image/svg+xml;utf8,<svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path d="M858.5 763.6c-18.9-44.7-56.9-79.6-103.3-96.8 53.4-43.9 88.8-110.8 88.8-184.5C844 401.8 722.2 280 554.7 280c-167.5 0-289.3 121.8-289.3 273.3 0 73.7 35.4 140.6 88.8 184.5-46.4 17.2-84.4 52.1-103.3 96.8-25.3 59.9-16.3 129.2 25 171.1 41.3 41.9 105.7 51.7 159.3 24.3 26.5-13.5 49.5-34.4 66.4-60.3 18.8 26.6 44.3 47 74.3 59.2 33.8 13.7 70.4 11.1 101-7.1 30.6-18.2 52.4-49.3 61.9-83.1 19.1-67.6 7.7-138.8-34.8-197.3zM352 654c-45.8-21.5-76.8-64.7-84.1-113.6-5.4-35.9 4.5-72.8 27.3-101.9 22.9-29.1 55.9-45.7 91.2-45.7 35.3 0 68.3 16.6 91.2 45.7 22.8 29.1 32.7 66 27.3 101.9-7.3 48.9-38.3 92.1-84.1 113.6-22.1 10.4-46.2 5.8-64.8-9.8-18.6-15.6-29.4-39.8-29.4-65.1 0-25.3 10.8-49.5 29.4-65.1 18.6-15.6 42.7-20.2 64.8-9.8 46.8 22 77.8 67.2 84.1 117.6 3.8 30.7-4.5 62.4-23.5 87.5-19 25.1-47.3 40.2-78.6 40.2s-59.6-15.1-78.6-40.2c-19-25.1-27.3-56.8-23.5-87.5 6.3-50.4 37.3-95.6 84.1-117.6 22.1-10.4 46.2-5.8 64.8 9.8 18.6 15.6 29.4 39.8 29.4 65.1 0 25.3-10.8 49.5-29.4 65.1-18.6 15.6-42.7 20.2-64.8 9.8z"/></svg>'); }
.score  { -webkit-mask-image: url('data:image/svg+xml;utf8,<svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path d="M864 170h-96c-4.4 0-8 3.6-8 8v822c0 4.4 3.6 8 8 8h96c17.7 0 32-14.3 32-32V202c0-17.7-14.3-32-32-32zM512 170h-96c-4.4 0-8 3.6-8 8v822c0 4.4 3.6 8 8 8h96c17.7 0 32-14.3 32-32V202c0-17.7-14.3-32-32-32zM160 170h-96c-17.7 0-32 14.3-32 32v774c0 17.7 14.3 32 32 32h96c4.4 0 8-3.6 8-8V178c0-4.4-3.6-8-8-8z"/></svg>'); }

.menu-text {
  transition: opacity 0.2s;
  opacity: 1;
}

/* 收起时隐藏文字 */
.sidebar.collapsed .menu-text {
  opacity: 0;
  width: 0;
  margin-left: 0 !important;
}

/* 收起展开按钮 */
.collapse-btn {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 20px;
  height: 60px;
  background: rgba(255, 255, 255, 0.1);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px 0 0 4px;
  transition: all 0.3s;
  writing-mode: vertical-rl;
}

.collapse-btn:hover {
  background: #1890ff;
}

.content-area {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  transition: margin-left 0.3s;
}
</style>