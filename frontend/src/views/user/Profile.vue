<template>
  <div>
    <el-card>
      <h3>个人信息</h3>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ user.realName }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ user.dept }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ user.position }}</el-descriptions-item>
        <el-descriptions-item label="手机">{{ user.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ user.email }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ user.role === 'admin' ? '管理员' : '员工' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ user.status === '1' ? '正常' : '禁用' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="margin-top: 20px;">
      <h3>修改密码</h3>
      <el-form :model="pwdForm" :rules="pwdRules" ref="pwdForm" label-width="100px" style="width: 400px;">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="pwdForm.oldPassword" type="password" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="pwdForm.newPassword" type="password" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="pwdForm.confirmPassword" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdatePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { updatePassword } from '@/api'

export default {
  name: 'UserProfile',
  data() {
    const validatePass = (rule, value, callback) => {
      if (value !== this.pwdForm.newPassword) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(sessionStorage.getItem('user') || '{}'),
      pwdForm: {},
      pwdRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async handleUpdatePassword() {
      this.$refs.pwdForm.validate(async valid => {
        if (valid) {
          await updatePassword({
            oldPassword: this.pwdForm.oldPassword,
            newPassword: this.pwdForm.newPassword
          })
          this.$message.success('密码修改成功，请重新登录')
          sessionStorage.removeItem('user')
          this.$router.push('/login')
        }
      })
    }
  }
}
</script>
