/**
 * @program: entfrm
 *
 * @description: 表单设计器操作记录器[增删改-操作记录池]
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-10
 **/


import {refactorArrayIndex} from '@/utils/formExpandDesign'
import {deepClone} from "@/utils";
import {vGet} from "@/utils/entfrm";
import {layoutComponents} from "@/components/FormExpandDesign/DesignConfig/config";
import {find} from "@riophae/vue-treeselect/src/utils";

export var addedPool = []

export var modifiedPool = []

/**
 * 存储单字段修改-功能后续开发中...
 * {
 *  formId: 101,
 *  记录字段:值,
 *  _state: "modified"
 * }
 */
const modifiedFieldPool = []

export var removedPool = []

/**
 * 保存新增数据
 */
export function savaAddedOption(data) {
  if (!vGet(data, 'config.formId')) return false
  if (layoutComponents.find(e => e.config.tagIcon == data.config.tagIcon)) return false
  if (Object.prototype.toString.call(data) === '[object Object]') {
    let item = deepClone(data)
    addedPool.push(Object.assign(item, {"_state": "added"}))
  }
}

/**
 * 保存修改数据
 */
export function savaModifiedOption(data) {
  if (!vGet(data, 'config.formId')) return false
  if (layoutComponents.find(e => e.config.tagIcon == data.config.tagIcon)) return false
  if (Object.prototype.toString.call(data) === '[object Object]') {
    let item = deepClone(data)
    //目标不在新增池中才添加
    if (addedPool.find(element => element.config.formId === item.config.formId) === undefined) {
      let obj = modifiedPool.find((e, i) => {
        if (e.config.formId === item.config.formId) {
          modifiedPool.splice(i, 1, Object.assign(item, {"_state": "modified"}))
          return true
        }
        return false
      })
      if (obj === undefined) {
        modifiedPool.push(Object.assign(item, {"_state": "modified"}))
      }
    } else {
      addedPool.find((e, i) => {
        if (e.config.formId === item.config.formId) {
          addedPool.splice(i, 1, Object.assign(item, {"_state": "added"}))
          return true
        }
        return false
      })
    }
  }
}


/**
 * 保存删除数据
 * @param {*}  data 保存的数据
 * @param {*}  notClearData 是否启动删除保存的数据删除
 * @param {*}
 */
export function savaRemovedOption(data, notClearData = false) {
  if (layoutComponents.find(e => e.config.tagIcon == data.config.tagIcon)) return false
  if (Object.prototype.toString.call(data) === '[object Object]' ||
    Object.prototype.toString.call(data) === '[object Array]') {

    //创建局部Pool,避免污染
    let item = deepClone(data)
    let letAddedPool = [...addedPool]
    let letModifiedPool = [...modifiedPool]
    //删除目标时要把目标在新增池跟修改池的数据清理干净
    letModifiedPool.forEach((element, index) => {
      if (Object.prototype.toString.call(item) === '[object Object]') {
        if (vGet(item, 'config.formId') === element.config.formId)
          modifiedPool.splice(index, 1)
      }
      if (Object.prototype.toString.call(item) === '[object Array]') {
        let delItem = item.find(e => vGet(e, 'config.formId') === element.config.formId)
        if (delItem !== undefined) {
          delete modifiedPool[index]
        }
      }
    })
    letAddedPool.forEach((element, index) => {
      if (Object.prototype.toString.call(item) === '[object Object]') {
        if (vGet(item, 'config.formId') === element.config.formId) {
          addedPool.splice(index, 1)
          if (!notClearData) item = undefined
        }

      }
      if (Object.prototype.toString.call(item) === '[object Array]') {
        let delItem = item.find(e => vGet(e, 'config.formId') === element.config.formId)
        if (delItem !== undefined) {
          delete addedPool[index]
          //过滤保存删除数组
          let letItem = [...item]
          letItem.forEach((e, index) => {
            if (vGet(e, 'config.formId') === element.config.formId) {
              delete item[index]
            }
          })
        }
      }
    })
    refactorArrayIndex(modifiedPool)
    refactorArrayIndex(addedPool)
    //保存删除数据
    if (item) {
      if (Object.prototype.toString.call(item) === '[object Array]') {
        item.forEach(element => removedPool.push(Object.assign(element, {"_state": "removed"})))
      } else {
        //查询池中是否有数据,有的话丢弃脏数据
        const index = removedPool.findIndex(e => e.config.formId === item.config.formId)
        if (index === -1) removedPool.push(Object.assign(item, {"_state": "removed"}))
      }
    }

  }
}


/**
 * 获取操作记录池数据
 *@Param {state} added|modified|removed。如传递null，则获取增删改数据。
 *@Param {onlyField} Boolean。传递true，modified的行数据将只返回修改的字段。[此功能后续开发]
 */
export function getChanges() {

  if (arguments.length === 0) {
    return addedPool.concat(modifiedPool).concat(removedPool)
  }

  if (arguments.length === 1 || arguments.length === 2) {
    switch (arguments[0]) {
      case 'added':
        return addedPool
        break;
      case 'modified':
        return modifiedPool
        break;
      case 'removed':
        return removedPool
        break;
      default:
        return []
    }
  }

}

export function clearOptionPool() {
  addedPool.length = 0
  modifiedPool.length = 0;
  removedPool.length = 0;
}
