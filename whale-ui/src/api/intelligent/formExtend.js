import request from '@/utils/request'

// 查询表单扩展列表
export function listFormExtend(query) {
  return request({
    url: '/intelligent/formExtend/list',
    method: 'get',
    params: query
  })
}

// 查询表单扩展详细
export function getFormExtend(fromExtendId) {
  return request({
    url: '/intelligent/formExtend/' + fromExtendId,
    method: 'get'
  })
}


// 查询表单扩展详细|级联
export function getFormExtendCascade(fromExtendId) {
  return request({
    url: '/intelligent/formExtend/cascade/' + fromExtendId,
    method: 'get'
  })
}


// 获取表单模板
export function fetchFormExtend(code) {
  return request({
    url: '/intelligent/formExtend/fetchFormExtend/' + code,
    method: 'get'
  })
}


// 新增表单扩展
export function addFormExtend(data) {
  return request({
    url: '/intelligent/formExtend/save',
    method: 'post',
    data: data
  })
}

// 修改表单扩展
export function editFormExtend(data) {
  return request({
    url: '/intelligent/formExtend/update',
    method: 'put',
    data: data
  })
}

// 删除表单扩展
export function delFormExtend(fromExtendId) {
  return request({
    url: '/intelligent/formExtend/remove/' + fromExtendId,
    method: 'delete'
  })
}
