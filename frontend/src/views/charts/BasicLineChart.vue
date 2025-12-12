<template>
  <div>
    <h2>📈 基础折线图 - 用户统计</h2>
    <p>展示从 Spring Boot 后端获取的用户数量变化趋势。</p>
    <div class="chart-container">
      <LineChart v-if="chartData.datasets.length" :data="chartData" :options="chartOptions" />
      <div v-else class="loading-placeholder">
        正在加载数据...
      </div>
    </div>
  </div>
</template>

<script>
import { Line as LineChart } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement } from 'chart.js';
import axios from 'axios';

// Registering necessary Chart.js components
ChartJS.register(Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement);

export default {
  name: 'BasicLineChart',
  components: {
    LineChart,
  },
  data() {
    return {
      // Structure for Chart.js data
      chartData: {
        labels: [], // e.g., ['User 1', 'User 2', ...]
        datasets: []
      },
      // Chart configuration options
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          title: {
            display: true,
            text: '用户 ID 趋势图'
          }
        },
        scales: {
          y: {
            title: {
              display: true,
              text: '用户 ID'
            }
          }
        }
      }
    };
  },
  mounted() {
    this.fetchUserData();
  },
  methods: {
    async fetchUserData() {
      // IMPORTANT: Replace with your actual backend URL if different
      const API_URL = '/api/users';
      try {
        const response = await axios.get(API_URL);
        const users = response.data;

        // --- Data Transformation for Line Chart ---
        // Assuming we want to plot the User ID over a sequence (their index in the array)

        const labels = users.map((user, index) => `User ${index + 1}`);
        const userIds = users.map(user => user.id); // Assuming 'id' is a numerical property in your Users entity

        this.chartData = {
          labels: labels,
          datasets: [
            {
              label: '用户 ID 值',
              backgroundColor: '#41B883',
              borderColor: '#41B883',
              data: userIds,
              fill: false, // Prevents filling the area under the line
              tension: 0.1 // Smoothness of the line
            }
          ]
        };

      } catch (error) {
        console.error('Error fetching user data:', error);
        // Optionally, handle error state here
      }
    }
  }
};
</script>

<style scoped>
.chart-container {
  height: 400px; /* Define a fixed height for the chart */
  width: 100%;
  margin-top: 20px;
}

/* Updated placeholder style */
.loading-placeholder {
  height: 400px;
  background-color: #e6f7ff;
  border: 1px solid #91d5ff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  color: #1890ff;
}
</style>