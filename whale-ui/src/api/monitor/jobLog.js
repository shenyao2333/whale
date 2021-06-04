import request from '@/utils/request'

// 查询调度日志列表
export function listJobLog(query) {
  return request({
    url: '/quartz/jobLog/list',
    method: 'get',
    params: query
  })
}

// 删除调度日志
export function delJobLog(id) {
  return request({
    url: '/quartz/jobLog/' + id,
    method: 'delete'
  })
}
