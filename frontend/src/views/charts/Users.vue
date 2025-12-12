<template>
  <div class="users-page">
    <h2>学生信息总表</h2>

    <div class="toolbar">
      <a-input-search
          v-model:value="keyword"
          placeholder="输入学号或姓名搜索"
          style="width: 300px"
          allow-clear
          enter-button
          @search="onSearch"
      />
      <a-button type="default" @click="loadData" style="margin-left: 12px">
        <template #icon><SyncOutlined :spin="loading" /></template>
        刷新
      </a-button>
    </div>

    <a-table
        :data-source="displayData"
        :columns="columns"
        :loading="loading"
        :scroll="{ x: 1400 }"
        :pagination="{ pageSize: 20, showQuickJumper: true, showTotal: total => `共 ${total} 条` }"
        row-key="id"
        bordered
    >
      <!-- 性别标签 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'gender'">
          <a-tag :color="record.gender === '男' ? 'blue' : 'pink'">
            {{ record.gender || '-' }}
          </a-tag>
        </template>
      </template>

      <template #emptyText>
        <a-empty description="暂无学生数据" />
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { message } from 'ant-design-vue'
import { SyncOutlined } from '@ant-design/icons-vue'

const loading = ref(false)
const keyword = ref('')
const allData = ref([])

// 列定义 —— 和后端 Users 实体完全一一对应
const columns = [
  { title: '序号', dataIndex: 'id', width: 80, fixed: 'left', align: 'center' },
  { title: '姓名', dataIndex: 'name', width: 100, fixed: 'left' },
  { title: '学号', dataIndex: 'studentNo', width: 130, fixed: 'left' },
  { title: '性别', dataIndex: 'gender', width: 70, align: 'center' },
  { title: '手机号码', dataIndex: 'mobile', width: 130 },
  { title: '学院', dataIndex: 'collegeId', width: 180 },
  { title: '班级', dataIndex: 'classId', width: 150 },
  { title: '入学日期', dataIndex: 'enrollDate', width: 120 },
  // 你实体里还有别的字段就继续往这里加就行
]

// 搜索过滤
const displayData = computed(() => {
  if (!keyword.value.trim()) return allData.value
  const kw = keyword.value.trim().toLowerCase()
  return allData.value.filter(item => {
    return (
        (item.name || '').toLowerCase().includes(kw) ||
        (item.studentNo || '').includes(kw)
    )
  })
})

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/users')  // 你的后端就是这个路径
    console.log('后端返回原始数据：', res.data)   // 关键！打开控制台看一眼
    allData.value = Array.isArray(res.data) ? res.data : []
    if (allData.value.length === 0) {
      message.info('当前没有学生数据')
    }
  } catch (err) {
    message.error('加载失败：' + (err.response?.data?.message || err.message))
    console.error(err)
    allData.value = []
  } finally {
    loading.value = false
  }
}

const onSearch = () => {
  // computed 会自动重新计算
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.users-page {
  padding: 24px;
  background: #fff;
  min-height: 100vh;
}

.toolbar {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
}
</style>