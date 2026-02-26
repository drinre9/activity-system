import request from '../utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'get'
  })
}

export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function getAllUsers() {
  return request({
    url: '/admin/users',
    method: 'get'
  })
}

export function addUser(data) {
  return request({
    url: '/admin/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/admin/user',
    method: 'put',
    data
  })
}

export function deleteUser(userId) {
  return request({
    url: '/admin/user/' + userId,
    method: 'delete'
  })
}

export function updatePassword(data) {
  return request({
    url: '/user/password',
    method: 'put',
    data
  })
}

export function getAllActivities() {
  return request({
    url: '/activities',
    method: 'get'
  })
}

export function getActiveActivities() {
  return request({
    url: '/activities/active',
    method: 'get'
  })
}

export function getActivityById(id) {
  return request({
    url: '/activity/' + id,
    method: 'get'
  })
}

export function getActivityList(params) {
  return request({
    url: '/activities/list',
    method: 'get',
    params
  })
}

export function addActivity(data) {
  return request({
    url: '/admin/activity',
    method: 'post',
    data
  })
}

export function updateActivity(data) {
  return request({
    url: '/admin/activity',
    method: 'put',
    data
  })
}

export function deleteActivity(id) {
  return request({
    url: '/admin/activity/' + id,
    method: 'delete'
  })
}

export function getAllRegistrations() {
  return request({
    url: '/admin/registrations',
    method: 'get'
  })
}

export function getRegistrationsByActivity(activityId) {
  return request({
    url: '/admin/registrations/' + activityId,
    method: 'get'
  })
}

export function getMyRegistrations() {
  return request({
    url: '/user/registrations',
    method: 'get'
  })
}

export function register(activityId) {
  return request({
    url: '/user/register/' + activityId,
    method: 'post'
  })
}

export function cancelRegistration(registrationId) {
  return request({
    url: '/user/cancel/' + registrationId,
    method: 'post'
  })
}

export function auditRegistration(data) {
  return request({
    url: '/admin/registration/audit',
    method: 'put',
    data
  })
}

export function getAllFeedbacks() {
  return request({
    url: '/admin/feedbacks',
    method: 'get'
  })
}

export function getMyFeedbacks() {
  return request({
    url: '/user/feedbacks',
    method: 'get'
  })
}

export function addFeedback(data) {
  return request({
    url: '/user/feedback',
    method: 'post',
    data
  })
}

export function deleteFeedback(id) {
  return request({
    url: '/admin/feedback/' + id,
    method: 'delete'
  })
}
