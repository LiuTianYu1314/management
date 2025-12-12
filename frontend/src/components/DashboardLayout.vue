<template>
  <div class="dashboard-container">
    <aside class="sidebar">
      <div class="logo">hello word</div>

      <nav class="menu">
        <router-link :to="{ name: 'Home' }" class="menu-item" exact-active-class="active">
          🏠 首页
        </router-link>

        <router-link :to="{ name: 'BasicLineChart' }" class="menu-item" exact-active-class="active">
          📈 折线图
        </router-link>
      </nav>
    </aside>

    <main class="content-area">
      <router-view />
    </main>
  </div>
</template>

<script setup>
// 使用 <script setup> 简化组件定义
</script>

<style scoped>
.dashboard-container {
  display: flex;
  height: 100vh; /* 占满整个视口高度 */
  background-color: #f0f2f5;
}

.sidebar {
  width: 200px; /* 左侧固定宽度 */
  background-color: #001529; /* 深色背景 */
  color: white;
  padding-top: 20px;
  flex-shrink: 0;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30px;
}

.menu-item {
  display: block;
  padding: 10px 20px;
  color: #a6adb4;
  text-decoration: none;
  transition: background-color 0.3s;
}

/* 修正了之前的样式冲突问题，只对 :hover 和 .active (精确匹配) 生效 */
.menu-item:hover {
  background-color: #1890ff; /* 选中/悬停状态 */
  color: white;
}

.content-area {
  flex-grow: 1; /* 占据剩余宽度 */
  padding: 20px;
  overflow-y: auto; /* 允许内容区滚动 */
}
.menu-item.active {
  background-color: #1890ff;
  color: white;
}
</style>