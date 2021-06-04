import request from '@/utils/request'

// 查询数据源列表
export function listDataset(query) {
  return request({
    url: '/toolkit/dataset/list',
    method: 'get',
    params: query
  })
}

// 解析脚本
export function analysisScripts(alias, scripts) {
  return request({
    url: '/toolkit/dataset/analysisScripts',
    method: 'get',
    params: {
      alias,
      scripts
    }
  })
}

// 预览数据
export function previewData(alias, scripts) {
  return request({
    url: '/toolkit/dataset/previewData',
    method: 'get',
    params: {
      alias,
      scripts
    }
  })
}

// 查询数据源详细
export function getDataset(id) {
  return request({
    url: '/toolkit/dataset/' + id,
    method: 'get'
  })
}

// 新增数据源
export function addDataset(data) {
  return request({
    url: '/toolkit/dataset/save',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function editDataset(data) {
  return request({
    url: '/toolkit/dataset/update',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDataset(id) {
  return request({
    url: '/toolkit/dataset/remove/' + id,
    method: 'delete'
  })
}



// 导出数据源
export function exportDataset(query) {
  return request({
    url: '/toolkit/dataset/export',
    method: 'get',
    params: query
  })
}
