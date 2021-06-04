import request from '@/utils/request'

// 查询菜单列表
export function listMenu(query) {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params: query
  })
}

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: '/system/menu/' + menuId,
    method: 'get'
  })
}

// 查询菜单下拉树结构
export function menuTree(query) {
  return request({
    url: '/system/menu/menuTree',
    method: 'get',
    params: query
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTree(roleId) {
  return request({
    url: '/system/menu/roleMenuTree/' + roleId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/system/menu/save',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function editMenu(data) {
  return request({
    url: '/system/menu/update',
    method: 'put',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: '/system/menu/remove/' + menuId,
    method: 'delete'
  })
}
