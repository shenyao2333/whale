/**
 * @program: entfrm
 *
 * @description: 用户Api
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-26
 **/
import request from '@/utils/request'
import {version} from "@/utils/formExpandDesign/formIntegrated";


// 查询用户列表
export function listDemoUser(query) {
  return request({
    url: '/intelligent/DemoUser/list',
    method: 'get',
    params: query
  })
}


// 查询用户详细
export function getDemoUser(id, formCode, versionIndex = 0) {
  return request({
    url: `/intelligent/DemoUser/getById/${id}/${formCode}/${version[versionIndex]}`,
    method: 'get'
  })
}


// 新增用户
export function addDemoUser(data) {
  return request({
    url: '/intelligent/DemoUser/save',
    method: 'post',
    data: data
  })
}


// 修改用户
export function editDemoUser(data) {
  return request({
    url: '/intelligent/DemoUser/update',
    method: 'put',
    data: data
  })
}


// 删除用户
export function delDemoUser(ids, formCode, versionIndex = 0) {
  return request({
    url: `/intelligent/DemoUser/remove/${ids}/${formCode}/${version[versionIndex]}`,
    method: 'delete'
  })
}
