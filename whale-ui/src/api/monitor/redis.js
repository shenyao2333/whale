import request from '@/utils/request'

// 查询redis详细
export function getRedis() {
  return request({
    url: '/monitor/redis',
    method: 'get'
  })
}
