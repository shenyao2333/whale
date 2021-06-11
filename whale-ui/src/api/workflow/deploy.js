import request from '@/utils/request'

// 查询表单管理列表
export function listForm(query) {
  return request({
    url: '/whale-workflow/engine-rest/deployment',
    method: 'get',
    params: query
  })
}
