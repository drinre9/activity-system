<template>
  <div>
    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="feedbackId" label="ID" width="60" />
      <el-table-column prop="activityName" label="活动名称" min-width="150" />
      <el-table-column prop="userName" label="员工姓名" width="100" />
      <el-table-column prop="rating" label="评分" width="120">
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
      <el-table-column label="操作" width="80">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getAllFeedbacks, deleteFeedback } from '@/api'

export default {
  name: 'AdminFeedback',
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
      const res = await getAllFeedbacks()
      this.tableData = res.data
      this.loading = false
    },
    async handleDelete(row) {
      await this.$confirm('确认删除此反馈?', '提示', { type: 'warning' })
      await deleteFeedback(row.feedbackId)
      this.loadData()
      this.$message.success('删除成功')
    },
    formatTime(time) {
      if (!time) return '-'
      return time.replace('T', ' ').substring(0, 16)
    }
  }
}
</script>
