/**
 * @program: entfrm
 *
 * @description:
 * JSX动态渲染控件插槽[目前针对于于DraggableItem.Vue中JSX渲染]
 * 将./slots中的文件挂载到对象componentChild上
 * 文件名为key，对应JSON配置中的__config__.tag
 * 文件内容为value，解析JSON配置中的__slot__
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-10
 **/

import {deepClone} from '@/utils/formExpandDesign/index'
import {makeMap} from "@/utils";
import {vGet} from "@/utils/entfrm";

//插槽集合
const componentChild = {}

// 参考https://github.com/vuejs/vue/blob/v2.6.10/src/platforms/web/server/util.js
// 制定内部不让props操作规范参数[这些都是关键字不能让你传参-内部操作使用]
const isAttr = makeMap(
  'accept,accept-charset,accesskey,action,align,alt,async,autocomplete,'
  + 'autofocus,autoplay,autosave,bgcolor,border,buffered,challenge,charset,'
  + 'checked,cite,class,code,codebase,color,cols,colspan,content,http-equiv,'
  + 'name,contenteditable,contextmenu,controls,coords,data,datetime,default,'
  + 'defer,dir,dirname,disabled,download,draggable,dropzone,enctype,method,for,'
  + 'form,formaction,headers,height,hidden,high,href,hreflang,http-equiv,'
  + 'icon,id,ismap,itemprop,keytype,kind,label,lang,language,list,loop,low,'
  + 'manifest,max,maxlength,media,method,GET,POST,min,multiple,email,file,'
  + 'muted,name,novalidate,open,optimum,pattern,ping,placeholder,poster,'
  + 'preload,radiogroup,readonly,rel,required,reversed,rows,rowspan,sandbox,'
  + 'scope,scoped,seamless,selected,shape,size,type,text,password,sizes,span,'
  + 'spellcheck,src,srcdoc,srclang,srcset,start,step,style,summary,tabindex,'
  + 'target,title,type,usemap,value,width,wrap'
)


//具体可参考:https://blog.csdn.net/viewyu12345/article/details/83012970
const slotsFiles = require.context('./slots', false, /\.js$/)
//获取插槽文件对象Key数组
const keys = slotsFiles.keys() || []
keys.forEach(key => {
  //获取插槽文件名(采用正则表达式|通过组$1获取)
  const tag = key.replace(/^\.\/(.*)\.\w+$/, '$1')
  //获取内部导入对象
  const value = slotsFiles(key).default
  //存入插槽集合
  componentChild[tag] = value
})

//制作v-model双向绑定[jsx内部不支持,这就是深入底层的代价]
function vModel(dataObject, config) {
  if (this.useVersion) {
    let formData = config.defaultValue || {}
    dataObject.props.value = formData.defaultValue
  } else {
    dataObject.props.value = config.defaultValue
  }
  dataObject.on.input = val => {
    this.$emit('input', val)
  }
}

//挂载控件插槽
function mountSlotFlies(h, confClone, children) {
  //获取目前控件的插槽对象
  const childObjs = componentChild[confClone.config.tag]
  if (childObjs) {
    //循环获取内部插槽函数
    Object.keys(childObjs).forEach(key => {
      const childFunc = childObjs[key]
      //目前控件插槽数据不为空
      if (confClone.slot && confClone.slot[key]) {
        //往目前控件中添加插槽
        children.push(childFunc(h, confClone, key))
      }
    })
  }
}

//构建template节点中使用的对象
function buildDataObject(confClone, dataObject) {

  //制作双向数据绑定::赋值操作
  if (vGet(confClone, 'config.vModel')) {
    vModel.call(this, dataObject, confClone.config)
  }

  //根据控件配置数据构建对应深入数据对象配置
  Object.keys(confClone).forEach(key => {
    const val = confClone[key]
    if (key === 'props') {
      Object.keys(val).forEach(k => {
        if (!isAttr(k)) dataObject.props[k] = val[k]
      })
    } else if (dataObject[key]) {
      //构建对应配置
      dataObject[key] = {...dataObject[key], ...val}
    }
  })
}

//template节点中使用的对象(声明)
function makeDataObject() {
  return {
    'class': {},
    attrs: {},
    props: {},
    nativeOn: {},
    on: {},
    style: {}
  }
}

export default {
  props: {
    conf: {
      type: Object,
      required: true
    },
    useVersion: {
      type: Boolean,
      default: false
    }
  },
  render(h) {

    //制作template节点中使用的对象
    const dataObject = makeDataObject()
    //克隆当前控件数据
    const confClone = deepClone(this.conf)
    //定义子级渲染数组
    const children = []

    // 如果slots文件夹存在与当前tag同名的文件，则执行文件中的代码
    mountSlotFlies.call(this, h, confClone, children)

    // 将json表单配置转化为vue render可以识别的 “数据对象（dataObject）”
    buildDataObject.call(this, confClone, dataObject)

    //(渲染函数 & JSX)参考:https://cn.vuejs.org/v2/guide/render-function.html
    return h(this.conf.config.tag, dataObject, children)
  }
}
