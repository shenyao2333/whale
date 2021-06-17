import request from '@/utils/request'

//
export function listForm(query) {
  return request({
    url: '/whale-workflow/engine-rest/deployment',
    method: 'get',
    params: query
  })
}


export function count(query) {
  return request({
    url: '/whale-workflow/engine-rest/deployment/count',
    method: 'get',
    params: query
  })
}
