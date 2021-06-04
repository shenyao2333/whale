import request from '@/utils/request'

// 查询表单管理列表
export function listForm(query) {
  return request({
    url: '/toolkit/form/list',
    method: 'get',
    params: query
  })
}

// 查询表单管理详细
export function getForm(id) {
  return request({
    url: '/toolkit/form/' + id,
    method: 'get'
  })
}

// 新增表单管理
export function addForm(data) {
  return request({
    url: '/toolkit/form/save',
    method: 'post',
    data: data
  })
}

// 修改表单管理
export function editForm(data) {
  return request({
    url: '/toolkit/form/update',
    method: 'put',
    data: data
  })
}

// 删除表单管理
export function delForm(id) {
  return request({
    url: '/toolkit/form/remove/' + id,
    method: 'delete'
  })
}

// 导出表单管理
export function exportForm(query) {
  return request({
    url: '/toolkit/form/export',
    method: 'get',
    params: query
  })
}

// 查询动态表单数据列表
export function listDynamicForm(query) {
  return request({
    url: '/toolkit/form/dynamicFormList',
    method: 'get',
    params: query
  })
}

// 查询动态表单数据
export function getDynamicForm(query) {
  return request({
    url: '/toolkit/form/dynamicForm',
    method: 'get',
    params: query
  })
}

// 新增动态表单管理
export function addDynamicForm(data) {
  return request({
    url: '/toolkit/form/dynamicFormSave',
    method: 'post',
    data: data
  })
}

// 删除动态表单数据
export function removeDynamicForm(query) {
  return request({
    url: '/toolkit/form/dynamicFormRemove',
    method: 'delete',
    params: query
  })
}

// 查询动态表单字段
export function getColumns(tableName) {
  return request({
    url: '/toolkit/form/column/' + tableName,
    method: 'get'
  })
}
