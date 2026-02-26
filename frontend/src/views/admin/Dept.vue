<template>
  <div>
    <el-button type="primary" @click="handleAdd" style="margin-bottom: 10px;">新增部门</el-button>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="deptId" label="ID" width="60" />
      <el-table-column prop="deptName" label="部门名称" />
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template slot-scope="scope">
          {{ formatTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="400px">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称" />
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
import { getAllDepts, addDept, deleteDept } from '@/api'

export default {
  name: 'AdminDept',
  data() {
    return {
      loading: false,
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      rules: {
        deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      const res = await getAllDepts()
      this.tableData = res.data
      this.loading = false
    },
    handleAdd() {
      this.form = {}
      this.dialogTitle = '新增部门'
      this.dialogVisible = true
    },
    async submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          await addDept(this.form)
          this.dialogVisible = false
          this.loadData()
          this.$message.success('操作成功')
        }
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除此部门?', '提示', { type: 'warning' })
      await deleteDept(row.deptId)
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
