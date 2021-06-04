import request from '@/utils/request'

// 查询流程列表
export function listProcess(query) {
  return request({
    url: '/activiti/process/list',
    method: 'get',
    params: query
  })
}

// 查询流程详细
export function getProcess(processId) {
  return request({
    url: '/activiti/process/' + processId,
    method: 'get'
  })
}


// 改变流程配置
export function changeStatus(procDefId, status) {
  const data = {
    procDefId,
    status
  }
  return request({
    url: '/activiti/process/changeStatus',
    method: 'put',
    params: data
  })
}

// 删除流程配置
export function delProcess(id) {
  return request({
    url: '/activiti/process/remove/' + id,
    method: 'delete'
  })
}


