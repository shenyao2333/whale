import request from '@/utils/request'

// 查询文件列表
export function listFileInfo(query) {
  return request({
    url: '/system/fileInfo/list',
    method: 'get',
    params: query
  })
}

// 查询文件详细
export function getFileInfo(fileInfoId) {
  return request({
    url: '/system/fileInfo/' + fileInfoId,
    method: 'get'
  })
}

// 新增文件配置
export function addFileInfo(data) {
  return request({
    url: '/system/fileInfo/save',
    method: 'post',
    data: data
  })
}

// 修改文件配置
export function editFileInfo(data) {
  return request({
    url: '/system/fileInfo/update',
    method: 'put',
    data: data
  })
}

// 删除文件配置
export function delFileInfo(fileInfoId) {
  return request({
    url: '/system/fileInfo/remove/' + fileInfoId,
    method: 'delete'
  })
}

