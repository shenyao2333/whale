/**
 * @program: entfrm
 *
 * @description:
 * 表单设计器核心参数配置
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-10
 **/


// 表单属性【右面板】
export const formConf = {
  // 表单域下组件的尺寸
  size: 'medium',
  // 标签对齐方式
  labelPosition: 'right',
  // 标签宽度
  labelWidth: 100,
  // row指定col间隔
  gutter: 15,
  // 是否禁用该表单内的所有组件
  disabled: false
}

// 输入型组件 【左面板】
// 具体参数配置详情可参考:https://cn.vuejs.org/v2/guide/render-function.html
export const inputComponents = [
  {
    // 组件的自定义配置[即代码中需要用到的数据,以后公共参数写这里面]
    config: {
      label: '单行文本',
      labelWidth: null,
      showLabel: true,
      changeTag: true,
      tag: 'el-input',
      tagIcon: 'input',
      defaultValue: undefined,
      required: true,
      layout: 'colFormItem',
      span: 24,
      document: 'https://element.eleme.cn/#/zh-CN/component/input',
      // 正则校验规则
      regList: [],
    },
    // 组件的插槽属性
    slot: {
      prepend: '',
      append: ''
    },
    // 普通的 HTML attribute
    attrs: {
      placeholder: '请输入',
      clearable: true,
      'prefix-icon': '',
      'suffix-icon': '',
      maxlength: null,
      'show-word-limit': false,
      readonly: false,
      disabled: false
    },
    // 与 `v-bind:style` 的 API 相同
    style: {width: '100%'}
  },
  {
    config: {
      label: '多行文本',
      labelWidth: null,
      showLabel: true,
      tag: 'el-input',
      tagIcon: 'textarea',
      defaultValue: undefined,
      required: true,
      layout: 'colFormItem',
      span: 24,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/input'
    },
    attrs: {
      type: 'textarea',
      placeholder: '请输入',
      autosize: {
        minRows: 4,
        maxRows: 4
      },
      maxlength: null,
      'show-word-limit': false,
      readonly: false,
      disabled: false
    },
    style: {width: '100%'},
  },
  {
    config: {
      label: '密码',
      showLabel: true,
      labelWidth: null,
      changeTag: true,
      tag: 'el-input',
      tagIcon: 'password',
      defaultValue: undefined,
      layout: 'colFormItem',
      span: 24,
      required: true,
      regList: [],
      document: 'https://element.eleme.cn/#/zh-CN/component/input'
    },
    slot: {
      prepend: '',
      append: ''
    },
    attrs: {
      placeholder: '请输入',
      'show-password': true,
      clearable: true,
      'prefix-icon': '',
      'suffix-icon': '',
      maxlength: null,
      'show-word-limit': false,
      readonly: false,
      disabled: false
    },
    style: {width: '100%'}
  },
  {
    config: {
      label: '计数器',
      showLabel: true,
      changeTag: true,
      labelWidth: null,
      tag: 'el-input-number',
      tagIcon: 'number',
      defaultValue: undefined,
      span: 24,
      layout: 'colFormItem',
      required: true,
      regList: [],
      document: 'https://element.eleme.cn/#/zh-CN/component/input-number'
    },
    attrs: {
      placeholder: '',
      min: undefined,
      max: undefined,
      step: 1,
      'step-strictly': false,
      precision: undefined,
      'controls-position': '',
      disabled: false
    }
  }
]

// 选择型组件 【左面板】
// 具体参数配置详情可参考:https://cn.vuejs.org/v2/guide/render-function.html
export const selectComponents = [
  {
    // 组件的自定义配置[即代码中需要用到的数据,以后公共参数写这里面]
    config: {
      label: '下拉选择',
      showLabel: true,
      labelWidth: null,
      tag: 'el-select',
      defaultValue: undefined,
      tagIcon: 'select',
      layout: 'colFormItem',
      span: 24,
      required: true,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/select'
    },
    // 组件的插槽属性
    slot: {
      options: [{
        label: '选项一',
        value: '1'
      }, {
        label: '选项二',
        value: '2'
      }]
    },
    // 普通的 HTML attribute
    attrs: {
      placeholder: '请选择',
      clearable: true,
      disabled: false,
      filterable: false,
      multiple: false
    },
    // 与 `v-bind:style` 的 API 相同
    style: {width: '100%'}
  },
  {
    config: {
      label: '级联选择',
      url: 'https://www.fastmock.site/mock/f8d7a54fb1e60561e2f720d5a810009d/fg/cascaderList',
      method: 'get',
      dataKey: 'list',
      showLabel: true,
      labelWidth: null,
      tag: 'el-cascader',
      tagIcon: 'cascader',
      layout: 'colFormItem',
      defaultValue: [],
      dataType: 'dynamic',
      span: 24,
      required: true,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/cascader'
    },
    'class': {
      props: {
        multiple: false,
        label: 'label',
        value: 'value',
        children: 'children'
      }
    },
    style: {width: '100%'},
    attrs: {
      placeholder: '请选择',
      'show-all-levels': true,
      disabled: false,
      clearable: true,
      filterable: false,
      separator: '/',
      options: [{
        id: 1,
        value: '1',
        label: '选项1',
        children: [{
          id: 2,
          value: '2',
          label: '选项1-1'
        }]
      }]
    }
  },
  {
    config: {
      label: '单选框组',
      labelWidth: null,
      showLabel: true,
      tag: 'el-radio-group',
      tagIcon: 'radio',
      changeTag: true,
      defaultValue: undefined,
      layout: 'colFormItem',
      span: 24,
      optionType: 'default',
      regList: [],
      required: true,
      border: false,
      document: 'https://element.eleme.cn/#/zh-CN/component/radio'
    },
    slot: {
      options: [{
        label: '选项一',
        value: '1'
      }, {
        label: '选项二',
        value: '2'
      }]
    },
    style: {},
    attrs: {
      size: 'medium',
      disabled: false
    }
  },
  {
    config: {
      label: '多选框组',
      tag: 'el-checkbox-group',
      tagIcon: 'checkbox',
      defaultValue: [],
      span: 24,
      showLabel: true,
      labelWidth: null,
      layout: 'colFormItem',
      optionType: 'default',
      required: true,
      regList: [],
      changeTag: true,
      border: false,
      document: 'https://element.eleme.cn/#/zh-CN/component/checkbox'
    },
    slot: {
      options: [{
        label: '选项一',
        value: '1'
      }, {
        label: '选项二',
        value: '2'
      }]
    },
    style: {},
    attrs: {
      size: 'medium',
      min: null,
      max: null,
      disabled: false
    }
  },
  {
    config: {
      label: '开关',
      tag: 'el-switch',
      tagIcon: 'switch',
      defaultValue: false,
      span: 24,
      showLabel: true,
      labelWidth: null,
      layout: 'colFormItem',
      required: true,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/switch'
    },
    style: {},
    attrs: {
      disabled: false,
      'active-text': '',
      'inactive-text': '',
      'active-color': null,
      'inactive-color': null,
      'active-value': true,
      'inactive-value': false
    }
  },
  {
    config: {
      label: '滑块',
      tag: 'el-slider',
      tagIcon: 'slider',
      defaultValue: null,
      span: 24,
      showLabel: true,
      layout: 'colFormItem',
      labelWidth: null,
      required: true,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/slider'
    },
    attrs: {
      disabled: false,
      min: 0,
      max: 100,
      step: 1,
      'show-stops': false,
      range: false
    }
  },
  {
    config: {
      label: '时间选择',
      tag: 'el-time-picker',
      tagIcon: 'time',
      defaultValue: null,
      span: 24,
      showLabel: true,
      layout: 'colFormItem',
      labelWidth: null,
      required: true,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/time-picker'
    },
    attrs: {
      placeholder: '请选择',
      disabled: false,
      clearable: true,
      'picker-options': {
        selectableRange: '00:00:00-23:59:59'
      },
      format: 'HH:mm:ss',
      'value-format': 'HH:mm:ss'
    },
    style: {width: '100%'}
  },
  {
    config: {
      label: '时间范围',
      tag: 'el-time-picker',
      tagIcon: 'time-range',
      span: 24,
      showLabel: true,
      labelWidth: null,
      layout: 'colFormItem',
      defaultValue: null,
      required: true,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/time-picker'
    },
    style: {width: '100%'},
    attrs: {
      disabled: false,
      clearable: true,
      'is-range': true,
      'range-separator': '至',
      'start-placeholder': '开始时间',
      'end-placeholder': '结束时间',
      format: 'HH:mm:ss',
      'value-format': 'HH:mm:ss'
    }
  },
  {
    config: {
      label: '日期选择',
      tag: 'el-date-picker',
      tagIcon: 'date',
      defaultValue: null,
      showLabel: true,
      labelWidth: null,
      span: 24,
      layout: 'colFormItem',
      required: true,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/date-picker'
    },
    attrs: {
      placeholder: '请选择',
      type: 'date',
      disabled: false,
      clearable: true,
      format: 'yyyy-MM-dd',
      'value-format': 'yyyy-MM-dd',
      readonly: false
    },
    style: {width: '100%'},
  },
  {
    config: {
      label: '日期范围',
      tag: 'el-date-picker',
      tagIcon: 'date-range',
      defaultValue: null,
      span: 24,
      showLabel: true,
      labelWidth: null,
      required: true,
      layout: 'colFormItem',
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/date-picker'
    },
    style: {width: '100%'},
    attrs: {
      type: 'daterange',
      'range-separator': '至',
      'start-placeholder': '开始日期',
      'end-placeholder': '结束日期',
      disabled: false,
      clearable: true,
      format: 'yyyy-MM-dd',
      'value-format': 'yyyy-MM-dd',
      readonly: false
    }
  },
  {
    config: {
      label: '评分',
      tag: 'el-rate',
      tagIcon: 'rate',
      defaultValue: 0,
      span: 24,
      showLabel: true,
      labelWidth: null,
      layout: 'colFormItem',
      required: true,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/rate'
    },
    style: {},
    attrs: {
      max: 5,
      'allow-half': false,
      'show-text': false,
      'show-score': false,
      disabled: false
    }
  },
  {
    config: {
      label: '颜色选择',
      tag: 'el-color-picker',
      tagIcon: 'color',
      span: 24,
      defaultValue: null,
      showLabel: true,
      labelWidth: null,
      layout: 'colFormItem',
      required: true,
      regList: [],
      changeTag: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/color-picker'
    },
    attrs: {
      'show-alpha': false,
      'color-format': '',
      disabled: false,
      size: 'medium'
    }
  }
]

// 布局型组件 【左面板】
export const layoutComponents = [
  {
    config: {
      layout: 'rowFormItem',
      tagIcon: 'row',
      label: '行容器',
      layoutTree: true,
      document: 'https://element.eleme.cn/#/zh-CN/component/layout#row-attributes',
      gutter: 0,
      span: 24,
      rowHeight: 160
    },
    attrs: {
      type: 'default',
      justify: 'start',
      align: 'top'
    }
  }
]
