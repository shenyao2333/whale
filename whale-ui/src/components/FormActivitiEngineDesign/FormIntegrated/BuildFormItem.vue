<script>
import render from '../Render/render'
import ruleTrigger from "@/components/FormActivitiEngineDesign/DesignConfig/ruleTrigger";


//暂时没有该控件的渲染布局元素-抽离公用
function layoutIsNotFound() {
  throw new Error(`没有与${this.currentItem.config.layout}匹配的layout`)
}


//布局元素渲染对象
const layouts = {
  //表单列布局元素(el-form->el-col)
  colFormItem(h, currentItem) {
    //获取目前控件config数据
    const config = currentItem.config
    //递归渲染子控件
    const child = renderChildren.apply(this, arguments)
    //目前标签宽度
    let labelWidth = config.labelWidth ? `${config.labelWidth}px` : null
    //是否显示标签
    if (config.showLabel === false) labelWidth = '0'
    // 是否非空[如果没有指定触发模式则,自动生成校验规则]
    const required = !ruleTrigger[config.tag] && config.required ? true : false

    //(渲染函数 & JSX)参考:https://cn.vuejs.org/v2/guide/render-function.html
    return (
      <el-col span={config.span}>
        <el-form-item label-width={labelWidth} prop={`${config.vModel}.defaultValue`}
                      label={config.showLabel ? config.label : ''} required={required}>
          <render key={config.renderKey} conf={currentItem} useVersion={true} onInput={event => {
            this.$set(config.defaultValue || {}, 'defaultValue', event)
          }}>
            {child}
          </render>
        </el-form-item>
      </el-col>
    )
  },
  //表单行容器布局元素(el-form->el-row)
  rowFormItem(h, currentItem) {
    //获取目前控件config数据
    const config = currentItem.config
    //递归渲染子控件
    let child = renderChildren.apply(this, arguments)
    //启动flex布局:https://element.eleme.cn/#/zh-CN/component/layout
    if (currentItem.type === 'flex') {
      child = <el-row type={currentItem.type} justify={currentItem.justify} align={currentItem.align}>
        {child}
      </el-row>
    }
    //(渲染函数 & JSX)参考:https://cn.vuejs.org/v2/guide/render-function.html
    return (
      <el-col span={config.span}>
        <el-row gutter={config.gutter}>{child}</el-row>
      </el-col>
    )
  }
}


//渲染子控件
function renderChildren(h, currentItem) {
  //获取当前子控件config数据
  const config = currentItem.config
  //递归结束条件-不存在||不为数组
  if (!Array.isArray(config.children)) return null
  //循环递归调用控件对应的布局元素函数
  return config.children.map((el) => {
    const layout = layouts[el.config.layout]
    if (layout) {
      return layout.call(this, h, el)
    }
    //暂时没有该控件的渲染布局元素
    return layoutIsNotFound.call(this)
  })
}


export default {
  props: ['currentItem'],
  render(h) {

    //获取当前控件对应的布局元素(需要渲染)
    const layout = layouts[this.currentItem.config.layout]

    if (layout) {
      //返回执行控件对应的布局元素函数
      return layout.call(this, h, this.currentItem)
    }

    //暂时没有该控件的渲染布局元素
    return layoutIsNotFound.call(this)
  }
}
</script>
