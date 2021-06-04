/**
 * @program: entfrm
 *
 * @description: 控件大管家增删改查Api
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-27
 **/
import request from '@/utils/request'

// 查询控件大管家列表
export function listControlSteward(query) {
  return request({
    url: '/intelligent/ControlSteward/list',
    method: 'get',
    params: query
  })
}

// 查询控件大管家详细
export function getControlSteward(id) {
  return request({
    url: '/intelligent/ControlSteward/' + id,
    method: 'get'
  })
}

// 新增控件大管家
export function addControlSteward(data) {
  return request({
    url: '/intelligent/ControlSteward/save',
    method: 'post',
    data: data
  })
}

// 修改控件大管家
export function editControlSteward(data) {
  return request({
    url: '/intelligent/ControlSteward/update',
    method: 'put',
    data: data
  })
}

// 删除控件大管家
export function delControlSteward(ids) {
  return request({
    url: '/intelligent/ControlSteward/remove/' + ids,
    method: 'delete'
  })
}
