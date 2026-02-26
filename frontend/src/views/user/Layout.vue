<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">员工中心</div>
      <el-menu :default-active="activeMenu" router>
        <el-menu-item index="/user/activity">
          <i class="el-icon-date"></i>
          <span>活动列表</span>
        </el-menu-item>
        <el-menu-item index="/user/my-registration">
          <i class="el-icon-tickets"></i>
          <span>我的报名</span>
        </el-menu-item>
        <el-menu-item index="/user/message">
          <el-badge v-if="unreadCount > 0" :value="unreadCount" class="badge" />
          <i class="el-icon-bell"></i>
          <span>我的消息</span>
        </el-menu-item>
        <el-menu-item index="/user/profile">
          <i class="el-icon-user"></i>
          <span>个人信息</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <span class="username">{{ user.realName }} - {{ user.dept }}</span>
        <el-button type="text" @click="handleLogout">退出登录</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { logout, getUnreadCount } from '@/api'
export default {
  name: 'UserLayout',
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('user') || '{}'),
      unreadCount: 0,
      timer: null
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    }
  },
  mounted() {
    this.loadUnreadCount()
    this.timer = setInterval(() => {
      this.loadUnreadCount()
    }, 30000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    async loadUnreadCount() {
      try {
        const res = await getUnreadCount()
        this.unreadCount = res.data.count
      } catch (e) {}
    },
    async handleLogout() {
      await logout()
      sessionStorage.removeItem('user')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.el-aside {
  background: #304156;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
}
.el-menu {
  border: none;
  background: #304156;
}
.el-menu-item {
  color: #bfcbd9;
}
.el-menu-item:hover,
.el-menu-item.is-active {
  background: #263445 !important;
  color: #409eff;
}
.el-header {
  background: #fff;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}
.username {
  margin-right: 20px;
  color: #333;
}
.el-main {
  background: #f0f2f5;
  padding: 20px;
}
.badge {
  position: absolute;
  left: 60px;
  top: 8px;
}
</style>
