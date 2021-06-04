import request from '@/utils/request'

// 查询字典类型列表
export function listDict(query) {
  return request({
    url: '/system/dict/list',
    method: 'get',
    params: query
  })
}

// 查询字典类型详细
export function getDict(dictId) {
  return request({
    url: '/system/dict/' + dictId,
    method: 'get'
  })
}

// 新增字典类型
export function addDict(data) {
  return request({
    url: '/system/dict/save',
    method: 'post',
    data: data
  })
}

// 修改字典类型
export function editDict(data) {
  return request({
    url: '/system/dict/update',
    method: 'put',
    data: data
  })
}

// 删除字典类型
export function delDict(dictId) {
  return request({
    url: '/system/dict/remove/' + dictId,
    method: 'delete'
  })
}

// 导出字典类型
export function exportDict(query) {
  return request({
    url: '/system/dict/export',
    method: 'get',
    params: query
  })
}

// 获取所有字典列表
export function dictList() {
  return request({
    url: '/system/dict/dictList',
    method: 'get'
  })
}
