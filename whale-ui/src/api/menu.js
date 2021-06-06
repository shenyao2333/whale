import request from '@/utils/request'

// 获取路由
export const getMenus = () => {
  return request({
    url: 'whale-system/system/menu',
    method: 'get'
  })
}
