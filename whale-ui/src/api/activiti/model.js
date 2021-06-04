import request from '@/utils/request'

// 查询模型列表
export function listModel(query) {
  return request({
    url: '/activiti/model/list',
    method: 'get',
    params: query
  })
}

// 查询模型详细
export function getModel(modelId) {
  return request({
    url: '/activiti/model/' + modelId,
    method: 'get'
  })
}


// 新增模型配置
export function addModel(data) {
  return request({
    url: '/activiti/model/save',
    method: 'post',
    data: data
  })
}

// 删除模型配置
export function delModel(id) {
  return request({
    url: '/activiti/model/remove/' + id,
    method: 'delete'
  })
}

// 部署模型
export function deployModel(id) {
  return request({
    url: '/activiti/model/deploy/' + id,
    method: 'post'
  })
}

