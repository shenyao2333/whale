import request from '@/utils/request'

// 查询部门列表
export function listDept(query) {
  return request({
    url: '/system/dept/list',
    method: 'get',
    params: query
  })
}

// 查询部门详细
export function getDept(deptId) {
  return request({
    url: '/system/dept/' + deptId,
    method: 'get'
  })
}

// 查询部门下拉树结构
export function deptTree() {
  return request({
    url: '/system/dept/deptTree',
    method: 'get'
  })
}

// 根据角色ID查询部门树结构
export function roleDeptTree(roleId) {
  return request({
    url: '/system/dept/roleDeptTree/' + roleId,
    method: 'get'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: '/system/dept/save',
    method: 'post',
    data: data
  })
}

// 修改部门
export function editDept(data) {
  return request({
    url: '/system/dept/update',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delDept(deptId) {
  return request({
    url: '/system/dept/remove/' + deptId,
    method: 'delete'
  })
}
