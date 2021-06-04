import request from '@/utils/request'

// 查询操作日志列表
export function list(query) {
  return request({
    url: '/monitor/operLog/list',
    method: 'get',
    params: query
  })
}

// 删除操作日志
export function delOperLog(id) {
  return request({
    url: '/monitor/operLog/remove/' + id,
    method: 'delete'
  })
}

// 清空操作日志
export function cleanOperLog() {
  return request({
    url: '/monitor/operLog/clean',
    method: 'delete'
  })
}

// 导出操作日志
export function exportOperLog(query) {
  return request({
    url: '/monitor/operLog/export',
    method: 'get',
    params: query
  })
}
