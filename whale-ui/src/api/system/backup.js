import request from '@/utils/request'

// 查询备份列表
export function listBackup(query) {
  return request({
    url: '/system/backup/list',
    method: 'get',
    params: query
  })
}

// 查询备份详细
export function getBackup(backupId) {
  return request({
    url: '/system/backup/' + backupId,
    method: 'get'
  })
}

// 新增备份配置
export function addBackup(data) {
  return request({
    url: '/system/backup/save',
    method: 'post',
    data: data
  })
}

// 修改备份配置
export function editBackup(data) {
  return request({
    url: '/system/backup/update',
    method: 'put',
    data: data
  })
}

// 删除备份配置
export function delBackup(backupId) {
  return request({
    url: '/system/backup/remove/' + backupId,
    method: 'delete'
  })
}

// 还原备份
export function restoreBackup(backupId) {
  return request({
    url: '/system/backup/restore/' + backupId,
    method: 'delete'
  })
}
