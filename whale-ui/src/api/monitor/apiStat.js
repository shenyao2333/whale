import request from '@/utils/request'

// 查询api统计详细
export function getApiStat() {
  return request({
    url: '/monitor/apiStat',
    method: 'get'
  })
}
