<template>
  <div>
    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="activityName" label="活动名称" min-width="150" />
      <el-table-column prop="location" label="地点" width="120" />
      <el-table-column prop="startTime" label="开始时间" width="160">
        <template slot-scope="scope">
          {{ formatTime(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="registrationTime" label="报名时间" width="160">
        <template slot-scope="scope">
          {{ formatTime(scope.row.registrationTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="warning">待审核</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="success">已确认</el-tag>
          <el-tag v-else-if="scope.row.status === '2'" type="danger">已拒绝</el-tag>
          <el-tag v-else type="info">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === '0'" size="mini" type="danger" @click="handleCancel(scope.row)">取消报名</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getMyRegistrations, cancelRegistration } from '@/api'

export default {
  name: 'UserMyRegistration',
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
      const res = await getMyRegistrations()
      this.tableData = res.data
      this.loading = false
    },
    async handleCancel(row) {
      await this.$confirm('确认取消报名?', '提示', { type: 'warning' })
      await cancelRegistration(row.registrationId)
      this.$message.success('取消成功')
      this.loadData()
    },
    formatTime(time) {
      if (!time) return '-'
      return time.replace('T', ' ').substring(0, 16)
    }
  }
}
</script>
