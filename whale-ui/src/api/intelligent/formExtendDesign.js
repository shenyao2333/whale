import request from '@/utils/request'


// 新增表单扩展
export function addFormExtendDesign(data) {
  return request({
    url: '/intelligent/FormExtendDesign/save',
    method: 'post',
    data: data
  })
}


// 新增表单扩展
export function editFormExtendDesign(data) {
  return request({
    url: '/intelligent/FormExtendDesign/update',
    method: 'put',
    data: data
  })
}

//数据映射表校验是否重复
export function tableValidator(table) {
  return request({
    url: '/intelligent/FormExtendDesign/tableValidator/' + table,
    method: 'get'
  })
}
