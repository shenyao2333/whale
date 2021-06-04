/**
 * @program: entfrm
 *
 * @description: 表单设计器[工具类]
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-10
 **/
import {vGet} from "@/utils/entfrm";

// 深拷贝对象
export function deepClone(obj) {

  // 定义ToString打印字符串方法
  const _toString = Object.prototype.toString

  // 校验 null, undefined, non-object, function
  if (!obj || typeof obj !== 'object') {
    return obj
  }

  // 拷贝节点(递归目前值处于cloneNode类型)
  if (obj.nodeType && 'cloneNode' in obj) {
    return obj.cloneNode(true)
  }

  // 数据转换Date(递归目前值处于Date类型)
  if (_toString.call(obj) === '[object Date]') {
    return new Date(obj.getTime())
  }

  // 创建正则表达式对象(递归目前值处于RegExp类型)
  if (_toString.call(obj) === '[object RegExp]') {
    const flags = []
    if (obj.global) {
      flags.push('g')
    }
    if (obj.multiline) {
      flags.push('m')
    }
    if (obj.ignoreCase) {
      flags.push('i')
    }

    return new RegExp(obj.source, flags.join(''))
  }

  //深度克隆数据对象返回[constructor:配置文件定义]
  const result = Array.isArray(obj) ? [] : vGet(obj, 'config.constructor') ? new obj.config.constructor() : {}

  //递归循环读取属性,并且重新赋值
  for (const key in obj) {
    result[key] = deepClone(obj[key])
  }

  return result
}


/**
 * 修改组件类型比较填充[内部对象克隆]
 * @param {*} target 目标[基类]
 * @param {*} source 源数据
 * @param {*}
 */
export function comparePadding(target, source) {

  // 定义ToString打印字符串方法
  const _toString = Object.prototype.toString

  // 校验 null, undefined, non-object, function
  if (!source || typeof source !== 'object') {
    return source
  }

  // 拷贝源节点数据(递归目前值处于cloneNode类型)
  if (source.nodeType && 'cloneNode' in source) {
    return source.cloneNode(true)
  }

  // 拷贝源时间数据(递归目前值处于Date类型)
  if (_toString.call(source) === '[object Date]') {
    return new Date(source.getTime())
  }

  // 拷贝源正则表达式对象(递归目前值处于RegExp类型)
  if (_toString.call(source) === '[object RegExp]') {
    const flags = []
    if (source.global) {
      flags.push('g')
    }
    if (source.multiline) {
      flags.push('m')
    }
    if (source.ignoreCase) {
      flags.push('i')
    }
    return new RegExp(source.source, flags.join(''))
  }

  //递归循比较填充
  for (const key in target) {
    if (source[key] !== undefined && typeof source[key] === typeof target[key]) {
      target[key] = comparePadding(target[key], source[key])
    }
  }

  return target
}


// 首字母大小
export function titleCase(str) {
  return str.replace(/( |^)[a-z]/g, L => L.toUpperCase())
}

// 下划转驼峰
export function camelCase(str) {
  return str.replace(/-[a-z]/g, str1 => str1.substr(-1).toUpperCase())
}

// 是否是数字
export function isNumberStr(str) {
  return /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(str)
}

// 判断是否为json数据
export function isJsonString(str) {
  try {
    if (typeof JSON.parse(str) == "object") {
      return true;
    }
  } catch (e) {
  }
  return false;
}

//美化代码配置
export const beautifierConf = {
  html: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'separate',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: false,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true
  },
  js: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'normal',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: true,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true
  }
}


/**
 * 获取修改过的字段
 * 比较两个对象值是否相等
 * 不相等-返回不相等的字段组合出的对象
 * @param {*} target 目标对象 旧
 * @param {*} source 源对象 新
 * 目前尚未开发完毕...
 */
export function getChangeField(target, source) {
  //父层接收抛射器
  let throwAccept = {}
  for (let key in target) {
    if (isObject(target[key]) && isObject(source[key])) {
      let throwAccept = getChangeField(target[key], source[key])
    }
    throwAccept[key] = source[key]
    /**
     * 子层像父层抛掷数据-父层接收
     * 一级一级的传递直到根层
     * 到时候如果有数据中比较有字段不一致的
     * 可以构建出层级关系的数据进行返回
     *
     * 后续开发中...
     *
     */
    return throwAccept
  }

}


/**
 * todo 判断基础数据类型专用
 * 判断两个对象内存地址是否一致
 * @param {*} object1 对象1
 * @param {*} object2 对象2
 */
function isMemoryAddressEqual(object1, object2) {
  if (object1 === object2) {
    // 添加了非零的y检查以使Flow满意，但这是多余的
    return object1 !== 0 || object2 !== 0 || 1 / object1 === 1 / object2
  } else {
    return object1 !== object1 && object2 !== object2
  }
}


/**
 * 判断两个对象数据是否一致
 * 注意:使用深比较,比较耗性能
 * @param {数组|对象} 目标-以他作为基准匹配 [旧对象]
 * @param {数组|对象} 来源 [新对象]
 */
export function isDataEqual(target, source) {
  //:todo 允许的类型,方便后期扩展
  const ALLOW_TYPE = {
    OBJECT: '[object Object]',
    ARRAY: '[object Array]',
    DATE: '[object Date]',
    FUNCTION: '[object Function]'
  }
  //:fixme 检测传入为数组或对象类型
  if (![ALLOW_TYPE.OBJECT, ALLOW_TYPE.ARRAY].includes(Object.prototype.toString.call(target)) &&
    ![ALLOW_TYPE.OBJECT, ALLOW_TYPE.ARRAY].includes(Object.prototype.toString.call(source))) {
    return false
  }
  //:fixme 比较两个对象的总体keys数量是否一致
  const targetKeys = Object.getOwnPropertyNames(target)
  const sourceKeys = Object.getOwnPropertyNames(source)
  if (targetKeys.length !== sourceKeys.length) {
    return false
  }
  //:todo 采用every查询元素是否一致,方便后期维护
  return targetKeys.every(element => {
    const TYPE = Object.prototype.toString.call(target[element])
    //:fixme 判断目前元素是否为对象类型-非空
    if (!!target[element] && TYPE === ALLOW_TYPE.OBJECT) {
      const CHILD_TYPE = source[element]
      if (TYPE !== Object.prototype.toString.call(CHILD_TYPE)) {
        return false
      }
      return isDataEqual(target[element], CHILD_TYPE)
    }
    //:fixme 判断目前元素是否为数组类型
    if (TYPE === ALLOW_TYPE.ARRAY) {
      //:fixme 检查两数组长度是否一致
      if (target[element].length !== source[element].length) {
        return false
      }
      //:fixme 判断数组内部数据是否一致
      return target[element].every((childElement, index) => {
        const CHILD_TYPE = Object.prototype.toString.call(childElement)
        //:fixme 判断目前元素是否为[数组|对象|函数]
        if ([ALLOW_TYPE.OBJECT, ALLOW_TYPE.FUNCTION, ALLOW_TYPE.ARRAY].includes(CHILD_TYPE)) {
          const CHILD_CHILD_TYPE = source[element][index]
          if (CHILD_TYPE !== Object.prototype.toString.call(CHILD_CHILD_TYPE)) {
            return false
          }
          return isDataEqual(childElement, CHILD_CHILD_TYPE)
        }
        //:fixme 基础数据类型判断
        if (!isMemoryAddressEqual(childElement, source[element][index])) {
          return false
        }
        return true
      })
    }
    //:fixme 判断目前元素是否为时间类型
    if (TYPE === ALLOW_TYPE.DATE) {
      return isMemoryAddressEqual(target[element].getTime(), source[element].getTime())
    }
    //:fixme 判断目前元素是否为函数类型
    if (TYPE === ALLOW_TYPE.FUNCTION) {
      return isMemoryAddressEqual(target[element].toString(), source[element].toString())
    }
    //:fixme 只要不是在ALLOW_TYPE内的统统全部认为基础数据类型判断
    if (!isMemoryAddressEqual(target[element], source[element])) {
      return false
    }
    //:fixme 两对象数据相等
    return true;
  })
}


/**
 * 重构数组索引
 * @param {array} 数组
 * @param {*}
 * @param {*}
 */
export function refactorArrayIndex(array) {
  if (Object.prototype.toString.call(array) !== "[object Array]") {
    return array
  }
  for (let i = 0; i < array.length; ++i) {
    if (!array[i]) {
      array.splice(i, 1)
      // i - 1 ,因为空元素在数组下标 2 位置，删除空之后，后面的元素要向前补位
      i = i - 1
    }
  }
}

/**
 * 拆分子数据::转为扁平化
 * @param {*} data 数据
 * @param {*}
 * @param {*}
 */
export function flatField(data) {
  // 定义ToString打印字符串方法
  const _toString = Object.prototype.toString

  //todo:定义递归对象返回 || 以及校验是否为空
  if (!data || _toString.call(data) !== '[object Array]') {
    return data
  }
  //todo:核心拆分操作
  let processField = []
  for (const item of data) {
    let config = item.config
    if (config.hasOwnProperty('children') && _toString.call(config.children) === '[object Array]') {
      processField = processField.concat(flatField(config.children))
    } else {
      processField.push(flatField(item))
    }
  }
  return processField;
}




















