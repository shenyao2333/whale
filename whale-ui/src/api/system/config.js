import request from '@/utils/request'

// 查询参数列表
export function listConfig(query) {
  return request({
    url: '/whale-system/system/config/list',
    method: 'get',
    params: query
  })
}

// 查询参数详细
export function getConfig(configId) {
  return request({
    url: '/whale-system/system/config/' + configId,
    method: 'get'
  })
}

// 根据参数键名查询参数值
export function getByKey(configKey) {
  return request({
    url: '/whale-system/system/config/getByKey/' + configKey,
    method: 'get'
  })
}

// 新增参数配置
export function addConfig(data) {
  return request({
    url: '/whale-system/system/config/save',
    method: 'post',
    data: data
  })
}

// 修改参数配置
export function editConfig(data) {
  return request({
    url: '/whale-system/system/config/update',
    method: 'put',
    data: data
  })
}

// 删除参数配置
export function delConfig(configId) {
  return request({
    url: '/whale-system/system/config/remove/' + configId,
    method: 'delete'
  })
}

