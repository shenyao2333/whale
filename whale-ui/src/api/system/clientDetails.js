import request from '@/utils/request'

// 查询终端列表
export function listClientDetails(query) {
  return request({
    url: '/system/clientDetails/list',
    method: 'get',
    params: query
  })
}

// 查询终端详细
export function getClientDetails(clientId) {
  return request({
    url: '/system/clientDetails/' + clientId,
    method: 'get'
  })
}

// 新增终端
export function addClientDetails(data) {
  return request({
    url: '/system/clientDetails/save',
    method: 'post',
    data: data
  })
}

// 修改终端
export function editClientDetails(data) {
  return request({
    url: '/system/clientDetails/update',
    method: 'put',
    data: data
  })
}

// 删除终端
export function delClientDetails(clientId) {
  return request({
    url: '/system/clientDetails/remove/' + clientId,
    method: 'delete'
  })
}
