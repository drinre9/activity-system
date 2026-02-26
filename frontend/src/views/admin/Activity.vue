<template>
  <div>
    <el-form :inline="true" :model="queryParams" class="query-form">
      <el-form-item label="活动名称">
        <el-input v-model="queryParams.activityName" placeholder="请输入活动名称" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="报名中" value="0" />
          <el-option label="进行中" value="1" />
          <el-option label="已结束" value="2" />
          <el-option label="已取消" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-button type="primary" @click="handleAdd" style="margin-bottom: 10px;">新增活动</el-button>

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
          <el-tag v-else-if="scope.row.status === '2'" type="info">已结束</el-tag>
          <el-tag v-else type="danger">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="活动名称" prop="activityName">
          <el-input v-model="form.activityName" />
        </el-form-item>
        <el-form-item label="活动类型" prop="activityType">
          <el-select v-model="form.activityType">
            <el-option label="体育运动" value="体育运动" />
            <el-option label="户外拓展" value="户外拓展" />
            <el-option label="文艺活动" value="文艺活动" />
            <el-option label="团建聚餐" value="团建聚餐" />
            <el-option label="志愿服务" value="志愿服务" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="最大人数" prop="maxParticipants">
          <el-input-number v-model="form.maxParticipants" :min="1" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="报名截止" prop="registrationDeadline">
          <el-date-picker v-model="form.registrationDeadline" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">报名中</el-radio>
            <el-radio label="1">进行中</el-radio>
            <el-radio label="2">已结束</el-radio>
            <el-radio label="3">已取消</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
import { getActivityList, addActivity, updateActivity, deleteActivity } from '@/api'

export default {
  name: 'AdminActivity',
  data() {
    return {
      loading: false,
      tableData: [],
      queryParams: {},
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      rules: {
        activityName: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        activityType: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
        location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        maxParticipants: [{ required: true, message: '请输入最大人数', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await getActivityList(this.queryParams)
        this.tableData = res.data
      } catch (e) {
        console.error('加载失败:', e)
      }
      this.loading = false
    },
    handleQuery() {
      this.loadData()
    },
    resetQuery() {
      this.queryParams = {}
      this.loadData()
    },
    handleAdd() {
      this.form = { status: '0', maxParticipants: 50, currentParticipants: 0 }
      this.dialogTitle = '新增活动'
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = { ...row }
      this.dialogTitle = '编辑活动'
      this.dialogVisible = true
    },
    async submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            if (this.form.activityId) {
              await updateActivity(this.form)
            } else {
              await addActivity(this.form)
            }
            this.dialogVisible = false
            this.loadData()
            this.$message.success('操作成功')
          } catch (e) {
            console.error('操作失败:', e)
            this.$message.error('操作失败: ' + (e.message || '未知错误'))
          }
        }
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除此活动?', '提示', { type: 'warning' })
      await deleteActivity(row.activityId)
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

<style scoped>
.query-form {
  margin-bottom: 15px;
}
</style>
