/**
 * @program: entfrm
 *
 * @description:
 * 目前实现表单独立
 * 表单设计器保存缓存[针对于表单控件新增]
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-10
 **/

// todo:--常量--
const DRAWING_ITEMS = 'drawingItems'
const DRAWING_ITEMS_VERSION = '1.2'
const DRAWING_ITEMS_VERSION_KEY = 'DRAWING_ITEMS_VERSION'
const DRAWING_ID = 'idGlobal'
const TREE_NODE_DATA = 'treeNodeData'
const DATA_CONF = 'dataConf'

// 获取目前躯干控件工程图
export function getDrawingList() {
  // 加入缓存版本的概念，保证缓存数据与程序匹配
  const version = localStorage.getItem(DRAWING_ITEMS_VERSION_KEY)
  if (version !== DRAWING_ITEMS_VERSION) {
    localStorage.setItem(DRAWING_ITEMS_VERSION_KEY, DRAWING_ITEMS_VERSION)
    saveDrawingList({})
    return {}
  }
  // 查找返回
  const str = localStorage.getItem(DRAWING_ITEMS)
  if (str) return JSON.parse(str)
  return {}
}

// 保存目前躯干控件工程图
export function saveDrawingList(list) {
  localStorage.setItem(DRAWING_ITEMS, JSON.stringify(list))
}

// 获取目前控件全局ID-内部标识
export function getIdGlobal() {
  // 加入缓存版本的概念，保证缓存数据与程序匹配
  const version = localStorage.getItem(DRAWING_ITEMS_VERSION_KEY)
  if (version !== DRAWING_ITEMS_VERSION) {
    localStorage.setItem(DRAWING_ITEMS_VERSION_KEY, DRAWING_ITEMS_VERSION)
    saveIdGlobal({})
    return {}
  }
  const str = localStorage.getItem(DRAWING_ID)
  if (str) return JSON.parse(str)
  return {}
}

// 保存目前控件全局ID-内部标识
export function saveIdGlobal(idGlobal) {
  localStorage.setItem(DRAWING_ID, JSON.stringify(idGlobal))
}

// 获取目前下拉树形选项局部ID-内部标识-目前实现控件独立
export function getTreeNodeData() {
  // 加入缓存版本的概念，保证缓存数据与程序匹配
  const version = localStorage.getItem(DRAWING_ITEMS_VERSION_KEY)
  if (version !== DRAWING_ITEMS_VERSION) {
    localStorage.setItem(DRAWING_ITEMS_VERSION_KEY, DRAWING_ITEMS_VERSION)
    saveTreeNodeData({})
    return {}
  }
  const str = localStorage.getItem(TREE_NODE_DATA)
  if (str) return JSON.parse(str)
  return {};
}

// 保存目前下拉树形选项局部ID-内部标识-目前实现控件独立
export function saveTreeNodeData(treeNodeData) {
  localStorage.setItem(TREE_NODE_DATA, JSON.stringify(treeNodeData))
}


// 获取数据配置缓存
export function getDataConf() {
  // 加入缓存版本的概念，保证缓存数据与程序匹配
  const version = localStorage.getItem(DRAWING_ITEMS_VERSION_KEY)
  if (version !== DRAWING_ITEMS_VERSION) {
    localStorage.setItem(DRAWING_ITEMS_VERSION_KEY, DRAWING_ITEMS_VERSION)
    savaDataConf({})
    return {}
  }
  const str = localStorage.getItem(DATA_CONF)
  if (str) return JSON.parse(str)
  return {};
}

// 保存数据配置缓存
export function savaDataConf(dataConf) {
  localStorage.setItem(DATA_CONF, JSON.stringify(dataConf))
}



