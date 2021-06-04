import request from '@/utils/request'

// 查询令牌
export function list(query) {
  return request({
    url: '/token/list',
    method: 'get',
    params: query
  })
}

// 删除令牌
export function delToken(token) {
  return request({
    url: '/token/' + token,
    method: 'delete'
  })
}
