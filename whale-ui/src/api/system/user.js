import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/entfrm";

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}

// 查询用户列表(不分页)
export function listAllUser(query) {
  return request({
    url: '/system/user/userList',
    method: 'get',
    params: query
  })
}


// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/system/user/' + praseStrEmpty(userId),
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/system/user/save',
    method: 'post',
    data: data
  })
}

// 修改用户
export function editUser(data) {
  return request({
    url: '/system/user/update',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delUser(userId) {
  return request({
    url: '/system/user/remove/' + userId,
    method: 'delete'
  })
}

// 导出用户
export function exportUser(query) {
  return request({
    url: '/system/user/exportUser',
    method: 'get',
    params: query
  })
}

// 用户密码重置
export function resetPwd(id, password) {
  const data = {
    id,
    password
  }
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: data
  })
}

// 用户状态修改
export function changeStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: data
  })
}

// 查询用户个人信息
export function getProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateProfile(data) {
  return request({
    url: '/system/user/updateProfile',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function updatePwd(password, newPassword) {
  const data = {
    password,
    newPassword
  }
  return request({
    url: '/system/user/updatePwd',
    method: 'put',
    params: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/system/user/updateAvatar',
    method: 'put',
    data: data
  })
}

// 下载用户导入模板
export function importTemplate() {
  return request({
    url: '/system/user/importTemplate',
    method: 'get'
  })
}
