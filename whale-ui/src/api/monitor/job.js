import request from '@/utils/request'

// 查询定时任务调度列表
export function listJob(query) {
  return request({
    url: '/quartz/job/list',
    method: 'get',
    params: query
  })
}

// 查询定时任务调度详细
export function getJob(jobId) {
  return request({
    url: '/quartz/job/' + jobId,
    method: 'get'
  })
}

// 新增定时任务调度
export function addJob(data) {
  return request({
    url: '/quartz/job',
    method: 'post',
    data: data
  })
}

// 修改定时任务调度
export function editJob(data) {
  return request({
    url: '/quartz/job',
    method: 'put',
    data: data
  })
}

// 删除定时任务调度
export function delJob(jobId) {
  return request({
    url: '/quartz/job/' + jobId,
    method: 'delete'
  })
}

// 导出定时任务调度
export function exportJob(query) {
  return request({
    url: '/quartz/job/export',
    method: 'get',
    params: query
  })
}

// 定时任务立即执行一次
export function runJob(jobName, jobGroup) {
  const data = {
    jobName,
    jobGroup
  }
  return request({
    url: '/quartz/job/runJob',
    method: 'post',
    data: data
  })
}

// 启动定时任务
export function startJob(jobId) {
  return request({
    url: '/quartz/job/startJob/' + jobId,
    method: 'post'
  })
}

// 暂停定时任务
export function stopJob(jobId) {
  return request({
    url: '/quartz/job/stopJob/' + jobId,
    method: 'post'
  })
}

// 启动全部定时任务
export function startJobs() {
  return request({
    url: '/quartz/job/startJobs',
    method: 'post'
  })
}

// 暂停全部定时任务
export function stopJobs() {
  return request({
    url: '/quartz/job/stopJobs',
    method: 'post'
  })
}

// 刷新全部定时任务
export function refreshJobs() {
  return request({
    url: '/quartz/job/refreshJobs',
    method: 'post'
  })
}

