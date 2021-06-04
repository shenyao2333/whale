import request from '@/utils/request'

// 获取路由
export const getMenus = () => {
  return request({
    url: '/system/menu',
    method: 'get'
  })
}
