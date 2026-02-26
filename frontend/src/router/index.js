import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import AdminLayout from '../views/admin/Layout.vue'
import UserLayout from '../views/user/Layout.vue'
import AdminActivity from '../views/admin/Activity.vue'
import AdminUser from '../views/admin/User.vue'
import AdminRegistration from '../views/admin/Registration.vue'
import AdminFeedback from '../views/admin/Feedback.vue'
import UserActivity from '../views/user/Activity.vue'
import UserMyRegistration from '../views/user/MyRegistration.vue'
import UserFeedback from '../views/user/Feedback.vue'
import UserProfile from '../views/user/Profile.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: 'activity', component: AdminActivity },
      { path: 'user', component: AdminUser },
      { path: 'registration', component: AdminRegistration },
      { path: 'feedback', component: AdminFeedback }
    ]
  },
  {
    path: '/user',
    component: UserLayout,
    children: [
      { path: 'activity', component: UserActivity },
      { path: 'my-registration', component: UserMyRegistration },
      { path: 'feedback', component: UserFeedback },
      { path: 'profile', component: UserProfile }
    ]
  }
]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

export default router
