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
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button v-if="scope.row.canJoin && scope.row.status === '0'" size="mini" type="primary" @click="handleRegister(scope.row)">报名</el-button>
          <el-button v-if="scope.row.canJoin" size="mini" @click="handleView(scope.row)">查看/评论</el-button>
          <span v-if="!scope.row.canJoin" style="color: #999;">不可参加</span>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="currentActivity.activityName" :visible.sync="dialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="活动类型">{{ currentActivity.activityType }}</el-descriptions-item>
        <el-descriptions-item label="活动地点">{{ currentActivity.location }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ formatTime(currentActivity.startTime) }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ formatTime(currentActivity.endTime) }}</el-descriptions-item>
        <el-descriptions-item label="报名人数">{{ currentActivity.currentParticipants }}/{{ currentActivity.maxParticipants }}</el-descriptions-item>
        <el-descriptions-item label="可参加部门">{{ currentActivity.deptNames }}</el-descriptions-item>
        <el-descriptions-item label="活动描述" :span="2">{{ currentActivity.description }}</el-descriptions-item>
      </el-descriptions>

      <el-divider>评论区</el-divider>
      
      <div class="comment-form">
        <el-input v-model="commentContent" type="textarea" :rows="2" placeholder="请输入评论内容" style="margin-bottom: 10px;" />
        <el-button type="primary" size="mini" @click="submitComment">发表评论</el-button>
      </div>

      <div class="comment-list">
        <div v-for="comment in commentList" :key="comment.commentId" class="comment-item">
          <div class="comment-header">
            <span class="comment-user">{{ comment.userName }}</span>
            <span class="comment-dept">({{ comment.deptName }})</span>
            <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
            <el-button v-if="isAdmin" size="mini" type="text" style="color: #f56c6c; margin-left: 10px;" @click="deleteComment(comment.commentId)">删除</el-button>
          </div>
          <div class="comment-content">{{ comment.content }}</div>
        </div>
        <div v-if="commentList.length === 0" style="color: #999; text-align: center; padding: 20px;">暂无评论</div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAllActivities, register, getComments, addComment, deleteComment, getDepts } from '@/api'

export default {
  name: 'UserActivity',
  data() {
    return {
      loading: false,
      tableData: [],
      user: JSON.parse(sessionStorage.getItem('user') || '{}'),
      deptList: [],
      dialogVisible: false,
      currentActivity: {},
      commentList: [],
      commentContent: '',
      isAdmin: false
    }
  },
  created() {
    this.isAdmin = this.user.role === 'admin'
    this.loadData()
    this.loadDepts()
  },
  methods: {
    async loadData() {
      this.loading = true
      const res = await getAllActivities()
      const activities = res.data
      
      const userDept = this.user.dept
      const userDeptId = this.deptList.find(d => d.deptName === userDept)?.deptId
      
      this.tableData = activities.map(a => {
        const deptIds = a.deptIds ? a.deptIds.split(',').map(d => parseInt(d)) : []
        const canJoin = deptIds.length === 0 || deptIds.includes(userDeptId)
        return { ...a, canJoin }
      }).sort((a, b) => {
        if (a.canJoin && !b.canJoin) return -1
        if (!a.canJoin && b.canJoin) return 1
        return 0
      })
      
      this.loading = false
    },
    async loadDepts() {
      const res = await getDepts()
      this.deptList = res.data
      this.loadData()
    },
    async handleRegister(row) {
      await this.$confirm('确认报名此活动?', '提示', { type: 'info' })
      await register(row.activityId)
      this.$message.success('报名成功')
      this.loadData()
    },
    async handleView(row) {
      this.currentActivity = row
      const res = await getComments(row.activityId)
      this.commentList = res.data
      this.dialogVisible = true
    },
    async submitComment() {
      if (!this.commentContent.trim()) {
        this.$message.warning('请输入评论内容')
        return
      }
      await addComment({
        activityId: this.currentActivity.activityId,
        content: this.commentContent
      })
      this.commentContent = ''
      const res = await getComments(this.currentActivity.activityId)
      this.commentList = res.data
      this.$message.success('评论成功')
    },
    async deleteComment(commentId) {
      await this.$confirm('确认删除此评论?', '提示', { type: 'warning' })
      await deleteComment(commentId)
      const res = await getComments(this.currentActivity.activityId)
      this.commentList = res.data
      this.$message.success('删除成功')
    },
    formatTime(time) {
      if (!time) return '-'
      return time.replace('T', ' ').substring(0, 16)
    }
  }
}
</script>

<style scoped>
.comment-form {
  margin-bottom: 15px;
}
.comment-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}
.comment-header {
  margin-bottom: 5px;
}
.comment-user {
  font-weight: bold;
  color: #409eff;
}
.comment-dept {
  color: #999;
  margin-left: 5px;
}
.comment-time {
  color: #999;
  margin-left: 10px;
  font-size: 12px;
}
.comment-content {
  color: #333;
}
</style>
