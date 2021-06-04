import request from '@/utils/request'

// 查询数据库列表
export function listDatasource(query) {
  return request({
    url: '/system/datasource/list',
    method: 'get',
    params: query
  })
}

// 查询数据库详细
export function getDatasource(datasourceId) {
  return request({
    url: '/system/datasource/' + datasourceId,
    method: 'get'
  })
}

// 新增数据库配置
export function addDatasource(data) {
  return request({
    url: '/system/datasource/save',
    method: 'post',
    data: data
  })
}

// 修改数据库配置
export function editDatasource(data) {
  return request({
    url: '/system/datasource/update',
    method: 'put',
    data: data
  })
}

// 删除数据库配置
export function delDatasource(datasourceId) {
  return request({
    url: '/system/datasource/remove/' + datasourceId,
    method: 'delete'
  })
}

// 查询数据库列表结构
export function datasourceList() {
  return request({
    url: '/system/datasource/datasourceList',
    method: 'get'
  })
}
