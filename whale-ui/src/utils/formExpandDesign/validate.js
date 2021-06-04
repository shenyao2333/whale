/**
 * @program: entfrm
 *
 * @description: 设计器保存校验
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-21
 *
 *
 * 1.数据配置[字段数量,数据映射表名]必填
 * 2.组件属性[字段名]必填
 * 3.控件数量必须小于业务映射表字段
 * 4.填写业务映射表名时判断数据库是否已经有了该表名
 * 5.控件渲染工程图数据不能为空
 **/
import {deepClone, flatField} from '@/utils/formExpandDesign'

/** 错误消息 **/
const messageInfo = {
  0: "目前检测到没有组件,请核对后再点击保存!",
  1: "已为你自动定位到字段名为空的组件,请核对后再点击保存!",
  2: "目前表单扩展ID为空,请核对后再点击保存!",
  3: "目前检测到设计器组件没有操作变化,请核对后再点击保存!",
  4: (componentSize, fieldSize) => `目前组件数量${componentSize}不能大于数据配置字段数量${fieldSize},请核对后再点击保存!`,
  5: "已为你自动定位到字段名重复的组件,请核对后再点击保存!",
}


/** 对象模式输出 **/
export default {
  //当前tab选择
  rightTab: {
    0: "field",
    1: "dataConfig"
  },
  //验证情况
  state: true,
  //输出消息信息
  message: '',
  //数据返回
  dataModel: {},
  //自动化激活ID
  activeId: undefined,
  //面板Tab显示
  currentTab: 'field',
  getCurrentTab: function () {
    return this.currentTab
  },
  setCurrentTab: function (currentTab) {
    this.currentTab = currentTab
    return this
  },
  getActiveId: function () {
    return this.activeId
  },
  setActiveId: function (activeId) {
    this.activeId = activeId
    return this
  },
  getDataModel: function () {
    return this.dataModel
  },
  setDataModel: function (dataModel) {
    this.dataModel = dataModel
    return this
  },
  getState: function () {
    return this.state
  },
  setState: function (state) {
    this.state = state
    return this
  },
  getMessage: function () {
    return this.message
  },
  setMessage: function (message) {
    this.message = message
    return this
  },
  designValidate: function (data, option) {

    this.initialization()
    let validate = ['', null, undefined, 0]
    let jsonMode = data.jsonMode, formExtendId = data.formExtendId, dataConf = jsonMode.dataConf
    //保留原数据
    let originJsonMode = deepClone(jsonMode)
    /** 判断非空 **/
    if (validate.includes([...jsonMode.drawingControls].length) && option === 0) {
      this.setState(false).setMessage(messageInfo[0]).setCurrentTab(this.rightTab[0])
      return this
    }
    /** 判断表单扩展非空 **/
    if (validate.includes(formExtendId)) {
      this.setState(false).setMessage(messageInfo[2]).setCurrentTab(this.rightTab[0])
      return this
    }
    /** 组件属性[字段名]必填 **/
    let flat = flatField(jsonMode.drawingControls)
    flat.find(item => {
      let config = item.config
      if (config.hasOwnProperty('vModel') && !config.vModel) {
        this.setState(false).setMessage(messageInfo[1]).setActiveId(config.formId).setCurrentTab(this.rightTab[0])
        return true
      }
      /** 组件属性[字段名]不能重复 **/
      let componentList = flat.filter(match => config.vModel === match.config.vModel)
      if (componentList.length > 1) {
        this.setState(false).setMessage(messageInfo[5]).setActiveId(config.formId).setCurrentTab(this.rightTab[0])
        return true
      }
    })
    if (!this.getState()) return this


    /** 控件数量必须小于字段数量 **/
    if (flat.length > dataConf.fieldLength) {
      this.setState(false).setMessage(messageInfo[4](flat.length, dataConf.fieldLength)).setCurrentTab(this.rightTab[1])
      return this
    }
    /** 修改,操作记录池中必须要有数据才能保存[就是要有操作变化] **/
    /*if(option===1&&validate.includes([...jsonMode.optionPool].length)){
      this.setState(false).setMessage(messageInfo[3]).setCurrentTab(this.rightTab[0])
      return this
    }*/
    /** 数据扁平化处理,方便后台使用数据 **/
    jsonMode.drawingControls = flat
    data.originJsonMode = originJsonMode
    this.setDataModel(data)

    return this
  },
  then: function (callback) {
    /** 使用箭头函数绑定函数当前this **/
    callback.call(this, this.state, this.message, this.dataModel, this.activeId, this.currentTab)
    return this
  },
  initialization: function () {
    this.setState(true).setActiveId(undefined).setMessage('')
      .setCurrentTab('field').setDataModel({})
  }
}






