<template>
  <div>
    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="messageId" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="content" label="内容" min-width="250" show-overflow-tooltip />
      <el-table-column prop="createByName" label="发送者" width="100" />
      <el-table-column prop="createTime" label="时间" width="180">
        <template slot-scope="scope">
          {{ formatTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleRead(scope.row)">查看</el-button>
          <el-button size="mini" type="text" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="消息详情" :visible.sync="dialogVisible" width="500px">
      <h3>{{ currentMessage.title }}</h3>
      <p style="color: #999; font-size: 12px; margin: 10px 0;">
        发送者：{{ currentMessage.createByName }} | 时间：{{ formatTime(currentMessage.createTime) }}
      </p>
      <p>{{ currentMessage.content }}</p>
      <span slot="footer">
        <el-button @click="handleDelete(currentMessage)">删除</el-button>
        <el-button type="primary" @click="dialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getMyMessages, deleteMessage, markAsRead } from '@/api'

export default {
  name: 'UserMessage',
  data() {
    return {
      loading: false,
      tableData: [],
      dialogVisible: false,
      currentMessage: {}
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      const res = await getMyMessages()
      this.tableData = res.data
      this.loading = false
    },
    async handleRead(row) {
      this.currentMessage = row
      await markAsRead(row.messageId)
      this.dialogVisible = true
      this.loadData()
      this.$parent.loadUnreadCount()
    },
    async handleDelete(row) {
      await this.$confirm('确认删除此消息?', '提示', { type: 'warning' })
      await deleteMessage(row.messageId)
      this.dialogVisible = false
      this.loadData()
      this.$parent.loadUnreadCount()
      this.$message.success('删除成功')
    },
    formatTime(time) {
      if (!time) return '-'
      return time.replace('T', ' ').substring(0, 19)
    }
  }
}
</script>
