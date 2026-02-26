<template>
  <div>
    <el-button type="primary" @click="handleAdd" style="margin-bottom: 10px;">发送消息</el-button>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="messageId" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="content" label="内容" min-width="200" show-overflow-tooltip />
      <el-table-column prop="deptName" label="接收部门" width="100" />
      <el-table-column prop="createByName" label="发送者" width="100" />
      <el-table-column prop="createTime" label="发送时间" width="180">
        <template slot-scope="scope">
          {{ formatTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="80">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入消息标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入消息内容" />
        </el-form-item>
        <el-form-item label="接收部门" prop="deptId">
          <el-select v-model="form.deptId" placeholder="请选择接收部门">
            <el-option v-for="dept in deptList" :key="dept.deptId" :label="dept.deptName" :value="dept.deptId" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getAllMessages, sendMessage, deleteMessage, getAllDepts } from '@/api'

export default {
  name: 'AdminMessage',
  data() {
    return {
      loading: false,
      tableData: [],
      deptList: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        deptId: [{ required: true, message: '请选择部门', trigger: 'change' }]
      }
    }
  },
  created() {
    this.loadData()
    this.loadDepts()
  },
  methods: {
    async loadData() {
      this.loading = true
      const res = await getAllMessages()
      this.tableData = res.data
      this.loading = false
    },
    async loadDepts() {
      const res = await getAllDepts()
      this.deptList = res.data
    },
    handleAdd() {
      this.form = {}
      this.dialogTitle = '发送消息'
      this.dialogVisible = true
    },
    async submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          await sendMessage(this.form)
          this.dialogVisible = false
          this.loadData()
          this.$message.success('发送成功')
        }
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除此消息?', '提示', { type: 'warning' })
      await deleteMessage(row.messageId)
      this.loadData()
      this.$message.success('删除成功')
    },
    formatTime(time) {
      if (!time) return '-'
      return time.replace('T', ' ').substring(0, 19)
    }
  }
}
</script>
