/**
 * @program: entfrm
 *
 * @description: 我发起的流程API
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-04-14
 **/

import request from '@/utils/request'


// 查询我发起流程列表
export function listIInitiatedProcess(query) {
  return request({
    url: '/activiti/IInitiatedProcess/list',
    method: 'get',
    params: query
  })
}

// 流程作废
export function abolishProcess(params) {
  return request({
    url: '/activiti/IInitiatedProcess/abolishProcess/',
    method: 'get',
    params: params
  })
}

// 查询流程历史跟踪列表
export function listDetailedProcess(query) {
  return request({
    url: '/activiti/IInitiatedProcess/detailedProcess',
    method: 'get',
    params: query
  })
}


