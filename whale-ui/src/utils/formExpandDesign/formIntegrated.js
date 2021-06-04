import request from "@/utils/request";
import ruleTrigger from "@/components/FormExpandDesign/DesignConfig/ruleTrigger";
import {fetchFormExtend} from "@/api/intelligent/formExtend";

/**
 * @program: entfrm
 *
 * @description: 集成form表单[合并]
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-25
 **/
import vue from 'vue'
import {inputComponents, selectComponents} from '@/components/FormExpandDesign/DesignConfig/config'
import {deepClone} from "@/utils";

/**
 * 预览整合Form表单操作对象
 * 公用对象
 * processIntegratedModel
 * @param {*} code 表单扩展查询编码
 * @param {*} formData 内部构建出属性绑定formData
 * @param {*} rules 内部构建出属性绑定rules
 */
export const formIntegrate = {
  async processIntegratedModel(buildSetObj, code, formData, rules) {
    let drawingControlsList = []
    await fetchFormExtend(code).then(response => drawingControlsList = JSON.parse(response.data))
    this.__bindBuildComponentRequiredData(drawingControlsList, buildSetObj, formData, rules)
    return drawingControlsList
  },
  async processIntegratedCustomizeModel(drawingList, buildSetObj, formData, rules) {
    let drawingControlsList = drawingList
    this.__bindBuildComponentRequiredData(drawingControlsList, buildSetObj, formData, rules)
    return drawingControlsList
  },
  __bindBuildComponentRequiredData(drawingControls, buildSetObj, formData, rules) {
    if (!Array.isArray(drawingControls)) return
    /** 组件请求统一 **/
    let componentPromise = {
      'el-cascade-options': async function (config) {
        let options = []
        try {
          await request({method: config.method, url: config.url}).then(response => {
            if (response.code === 0) {
              let {data} = response
              options = data[config.dataKey]
            } else {
              this.msgError(response.msg);
            }
          })
        } catch (err) {
        }
        return options
      }
    }
    for (const element of drawingControls) {
      const config = element.config || {}, attrs = element.attrs || {}
      /** 构建表单data **/
      this.__buildData(buildSetObj, config, formData)
      /** 构建表单规则 **/
      if (buildSetObj) {
        this.__buildRules(config, attrs, rules)
      }

      /** 处理级联的动态下拉 **/
      if (config.dataType === 'dynamic' && config.tagIcon === 'cascader') {
        componentPromise['el-cascade-options'].call(this, config).then(e => attrs.options = e)
      }

      /** 处理行布局组件子级组件属性 **/
      if (config.children) {

        this.__bindBuildComponentRequiredData(config.children, buildSetObj, formData, rules)
      }
    }
  },
  /** 构建表单data **/
  __buildData(buildSetObj, config, formData) {
    if (buildSetObj) {
      if (config.vModel === undefined) return
      /** 添加表单数据(控件默认值) **/
      vue.set(formData, config.vModel, {defaultValue: config.defaultValue, tagIcon: config.tagIcon})
    } else {
      config.defaultValue = formData[config.vModel]
    }
  },
  /** 构建表单规则 **/
  __buildRules(config, attrs, Rules) {
    if (config.vModel === undefined) return
    if (ruleTrigger[config.tag]) {
      let rules = []
      if (config.required) {
        let temp = {}
        // 多选类型 && 错误消息
        if (Array.isArray(config.defaultValue)) {
          temp["type"] = 'array'
          temp["message"] = `请至少选择一个${config.label}`
        } else {
          temp["message"] = (attrs.placeholder || `${config.label}不能为空`)
        }
        // 添加表单规则
        temp["required"] = true
        temp["trigger"] = ruleTrigger[config.tag]
        rules.push(temp)
      }
      // 正则表达式
      if (config.regList && Array.isArray(config.regList)) {
        // 合并组件内部正则表达式
        config.regList.forEach(item => {
          // 添加表单规则
          if (item.pattern) rules.push(Object.assign(item, {trigger: ruleTrigger[config.tag]}))
        })
      }
      //添加表单规则
      if (rules.length > 0) Rules[`${config.vModel}.defaultValue`] = rules
    }
  }
}


/** 合并表单数据 **/
export async function mergeFormData(formCode, form, rules, customizeForm, customizeRules) {
  await formIntegrate.processIntegratedModel(true, formCode, form, rules)
  for (const key in customizeForm) {
    vue.set(form, key, customizeForm[key])
  }
  for (const key in customizeRules) {
    rules[key] = customizeRules[key]
  }
}


// 表单动态undefined
export function resetFormFields(form, thisForm) {
  let componentList = [...inputComponents, ...selectComponents]
  let _toString = Object.prototype.toString
  for (let key in form) {
    if (_toString.call(form[key]) === '[object Object]' && form[key].tagIcon && !thisForm.hasOwnProperty(key)) {
      let component = componentList.find(item => form[key].tagIcon === item.config.tagIcon)
      form[key].defaultValue = (component && component.config.defaultValue) || undefined
    } else {
      delete form[key]
    }
  }
  for (let key in thisForm) {
    vue.set(form, key, thisForm[key])
  }
}


/** 版本号统一 [初始三个版本号] todo:后期可自行添加 **/
export const version = [
  1.1,
  1.2,
  1.3
]


/**
 * 制作集成合并数据新增与修改
 * @param {*} formCode 编码
 * @param {*} versionIndex 序列编码,默认选择第0个版本
 * @param {*}
 */
export function makeIntegrated(formCode, formData, versionIndex = 0) {
  return {
    formCode: formCode,
    formData: formData,
    businessVersion: version[versionIndex]
  }
}




