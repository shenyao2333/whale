import request from '@/utils/request'

// 查询接口列表
export function listApiinfo(query) {
  return request({
    url: '/api/apiinfo/list',
    method: 'get',
    params: query
  })
}

// 查询接口详细
export function getApiinfo(id) {
  return request({
    url: '/api/apiinfo/' + id,
    method: 'get'
  })
}

// 快速新增接口
export function addApiinfos(data) {
  return request({
    url: '/api/apiinfo/quickSave',
    method: 'post',
    data: data
  })
}

// 新增接口
export function addApiinfo(data) {
  return request({
    url: '/api/apiinfo/save',
    method: 'post',
    data: data
  })
}

// 修改接口
export function editApiinfo(data) {
  return request({
    url: '/api/apiinfo/update',
    method: 'put',
    data: data
  })
}

// 发布接口
export function publishApiinfo(id) {
  console.log(id)
  return request({
    url: '/api/apiinfo/publish/' + id,
    method: 'put'
  })
}

// 删除接口
export function delApiinfo(id) {
  return request({
    url: '/api/apiinfo/remove/' + id,
    method: 'delete'
  })
}



// 导出接口
export function exportApiinfo(query) {
  return request({
    url: '/api/apiinfo/export',
    method: 'get',
    params: query
  })
}
