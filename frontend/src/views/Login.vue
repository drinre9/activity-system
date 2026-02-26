<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="title">员工团队建设活动管理系统</h2>
      <el-form :model="loginForm" @submit.native.prevent>
        <el-form-item>
          <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="el-icon-user" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%;" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="tips">
        <p>管理员账号: admin / admin123</p>
        <p>员工账号: zhangsan / user123</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import { login } from '@/api'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      if (!this.loginForm.username || !this.loginForm.password) {
        this.$message.warning('请输入用户名和密码')
        return
      }
      this.loading = true
      try {
        const res = await login(this.loginForm)
        if (res.code === 200) {
          sessionStorage.setItem('user', JSON.stringify(res.data.user))
          const role = res.data.user.role
          if (role === 'admin') {
            this.$router.push('/admin/activity')
          } else {
            this.$router.push('/user/activity')
          }
        } else {
          this.$message.error(res.message)
        }
      } catch (e) {
        this.$message.error('登录失败')
      }
      this.loading = false
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background:deg, #667 linear-gradient(135eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
  padding: 20px;
}
.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
.tips {
  text-align: center;
  color: #999;
  font-size: 12px;
}
.tips p {
  margin: 5px 0;
}
</style>
