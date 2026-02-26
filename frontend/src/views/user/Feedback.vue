<template>
  <div>
    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="activityName" label="活动名称" min-width="150" />
      <el-table-column prop="rating" label="评分" width="150">
        <template slot-scope="scope">
          <el-rate v-model="scope.row.rating" disabled />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="反馈内容" min-width="200" show-overflow-tooltip />
      <el-table-column prop="feedbackTime" label="反馈时间" width="160">
        <template slot-scope="scope">
          {{ formatTime(scope.row.feedbackTime) }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getMyFeedbacks } from '@/api'

export default {
  name: 'UserFeedback',
  data() {
    return {
      loading: false,
      tableData: []
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      const res = await getMyFeedbacks()
      this.tableData = res.data
      this.loading = false
    },
    formatTime(time) {
      if (!time) return '-'
      return time.replace('T', ' ').substring(0, 16)
    }
  }
}
</script>
