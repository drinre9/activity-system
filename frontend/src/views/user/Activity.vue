<template>
  <div>
    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="activityId" label="ID" width="60" />
      <el-table-column prop="activityName" label="活动名称" min-width="150" />
      <el-table-column prop="activityType" label="类型" width="100" />
      <el-table-column prop="location" label="地点" width="120" />
      <el-table-column prop="startTime" label="开始时间" width="160">
        <template slot-scope="scope">
          {{ formatTime(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="currentParticipants" label="报名人数" width="100">
        <template slot-scope="scope">
          {{ scope.row.currentParticipants }}/{{ scope.row.maxParticipants }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">报名中</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="primary">进行中</el-tag>
          <el-tag v-else type="info">已结束</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === '0'" size="mini" type="primary" @click="handleRegister(scope.row)">报名</el-button>
          <el-button v-else-if="scope.row.status === '2'" size="mini" type="warning" @click="handleFeedback(scope.row)">反馈</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="提交反馈" :visible.sync="feedbackDialogVisible" width="500px">
      <el-form :model="feedbackForm" :rules="feedbackRules" ref="feedbackForm" label-width="80px">
        <el-form-item label="活动">
          <el-input v-model="feedbackForm.activityName" disabled />
        </el-form-item>
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="feedbackForm.rating" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="feedbackForm.content" type="textarea" :rows="4" placeholder="请填写活动反馈" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="feedbackDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFeedback">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getAllActivities, register, addFeedback } from '@/api'

export default {
  name: 'UserActivity',
  data() {
    return {
      loading: false,
      tableData: [],
      feedbackDialogVisible: false,
      feedbackForm: {},
      feedbackRules: {
        rating: [{ required: true, message: '请选择评分', trigger: 'change' }],
        content: [{ required: true, message: '请填写反馈内容', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      const res = await getAllActivities()
      this.tableData = res.data
      this.loading = false
    },
    async handleRegister(row) {
      await this.$confirm('确认报名此活动?', '提示', { type: 'info' })
      await register(row.activityId)
      this.$message.success('报名成功')
      this.loadData()
    },
    handleFeedback(row) {
      this.feedbackForm = { activityId: row.activityId, activityName: row.activityName, rating: 5, content: '' }
      this.feedbackDialogVisible = true
    },
    async submitFeedback() {
      this.$refs.feedbackForm.validate(async valid => {
        if (valid) {
          await addFeedback(this.feedbackForm)
          this.feedbackDialogVisible = false
          this.$message.success('提交成功')
        }
      })
    },
    formatTime(time) {
      if (!time) return '-'
      return time.replace('T', ' ').substring(0, 16)
    }
  }
}
</script>
