import request from '@/utils/request'
import {version} from "@/utils/formExpandDesign/formIntegrated";

// 查询动态表单列表
export function listFormActivitiEngine(query) {
  return request({
    url: '/activiti/FormActivitiEngine/list',
    method: 'get',
    params: query
  })
}

// 查询动态表单详细
export function getFormActivitiEngine(id) {
  return request({
    url: '/activiti/FormActivitiEngine/' + id,
    method: 'get'
  })
}



// 获取动态表单模板
export function fetchFormActivitiEngine(code) {
  return request({
    url: '/activiti/FormActivitiEngine/fetchFormActivitiEngine/' + code,
    method: 'get'
  })
}


// 新增动态表单
export function addFormActivitiEngine(data) {
  return request({
    url: '/activiti/FormActivitiEngine/save',
    method: 'post',
    data: data
  })
}

// 修改动态表单
export function editFormActivitiEngine(data) {
  return request({
    url: '/activiti/FormActivitiEngine/update',
    method: 'put',
    data: data
  })
}

// 删除动态表单
export function delFormActivitiEngine(id) {
  return request({
    url: '/activiti/FormActivitiEngine/remove/' + id,
    method: 'delete'
  })
}


// 修改动态表单设计器
export function editFormActivitiEngineDesign(data) {
  return request({
    url: '/activiti/FormActivitiEngine/design/update',
    method: 'put',
    data: data
  })
}

// 查询动态表单设计器
export function selectFormActivitiEngineDesign(id) {
  return request({
    url: '/activiti/FormActivitiEngine/design/select/' + id,
    method: 'get',
  })
}

// 查询业务数据
export function getDemoUser(query) {
  return request({
    url: `/activiti/FormActivitiEngine/design/select/`,
    method: 'get',
    params: query
  })
}
