// 批次管理相关API
import request from './request'

// 获取批次列表（带分页）
export function getBatchList(params) {
  return request({
    url: '/api/batch/list',
    method: 'get',
    params
  })
}

// 获取未分配的项目列表
export function getUnassignedProjects(params) {
  return request({
    url: '/api/batch/unassigned-projects',
    method: 'get',
    params
  })
}

// 创建新批次
export function createBatch(data) {
  return request({
    url: '/api/batch/create',
    method: 'post',
    data
  })
}

// 编辑批次
export function updateBatch(id, data) {
  return request({
    url: `/api/batch/update/${id}`,
    method: 'put',
    data
  })
}

// 删除批次
export function deleteBatch(id) {
  return request({
    url: `/api/batch/delete/${id}`,
    method: 'delete'
  })
}

// 获取批次详情
export function getBatchDetails(id) {
  return request({
    url: `/api/batch/details/${id}`,
    method: 'get'
  })
}
