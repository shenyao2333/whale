<script>
import draggable from 'vuedraggable'
import render from '../Render/render'


//暂时没有该控件的渲染布局元素-抽离公用
function layoutIsNotFound() {
  throw new Error(`没有与${this.currentItem.config.layout}匹配的layout`)
}

//公用组件抽离对象
const components = {
  //控件项按钮
  itemBtns(h, currentItem, index, list) {
    //获取父组件copyItem,deleteItem事件
    const {copyItem, deleteItem} = this.$listeners
    //(渲染函数 & JSX)参考:https://cn.vuejs.org/v2/guide/render-function.html
    return [
      <span class="drawing-item-copy" title="复制" onClick={event => {
        copyItem(currentItem, list);
        event.stopPropagation()
      }}>
        <i class="el-icon-copy-document"/>
      </span>,
      <span class="drawing-item-delete" title="删除" onClick={event => {
        deleteItem(index, list);
        event.stopPropagation()
      }}>
        <i class="el-icon-delete"/>
      </span>
    ]
  }
}

//布局元素渲染对象
const layouts = {
  //表单列布局元素(el-form->el-col)
  colFormItem(h, currentItem, index, list) {
    //获取父组件activeItem事件
    const {activeItem} = this.$listeners
    //获取目前控件config数据
    const config = currentItem.config
    //递归渲染子控件
    const child = renderChildren.apply(this, arguments)
    //给目前处于激活状态下的控件添加样式
    let className = this.activeId === config.formId ? 'drawing-item active-from-item' : 'drawing-item'
    //目前标签宽度
    let labelWidth = config.labelWidth ? `${config.labelWidth}px` : null
    //是否显示标签
    if (config.showLabel === false) labelWidth = '0'
    //(渲染函数 & JSX)参考:https://cn.vuejs.org/v2/guide/render-function.html
    return (
      <el-col span={config.span} class={className}
              nativeOnClick={event => {
                activeItem(currentItem);
                event.stopPropagation()
              }}>
        <el-form-item label-width={labelWidth}
                      label={config.showLabel ? config.label : ''} required={config.required}>
          <render key={config.renderKey} conf={currentItem} onInput={event => {
            this.$set(config, 'defaultValue', event)
          }}>
            {child}
          </render>
        </el-form-item>
        {components.itemBtns.apply(this, arguments)}
      </el-col>
    )
  },
  //表单行容器布局元素(el-form->el-row)
  rowFormItem(h, currentItem, index, list) {
    //获取父组件activeItem事件
    const {activeItem} = this.$listeners
    //获取目前控件config数据
    const config = currentItem.config
    //给目前处于激活状态下的控件添加样式
    const className = this.activeId === config.formId
      ? 'drawing-row-item active-from-item'
      : 'drawing-row-item'
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
        <el-row gutter={config.gutter} class={className}
                nativeOnClick={event => {
                  activeItem(currentItem);
                  event.stopPropagation()
                }}>
          <span class="component-name">{config.componentName}</span>
          <draggable list={config.children || []} animation={340}
                     group="componentsGroup" class="drag-wrapper" style={`height:${config.rowHeight}px`}>
            {child}
          </draggable>
          {components.itemBtns.apply(this, arguments)}
        </el-row>
      </el-col>
    )
  }
}


//渲染子控件
function renderChildren(h, currentItem, index, list) {
  //获取当前子控件config数据
  const config = currentItem.config
  //递归结束条件-不存在||不为数组
  if (!Array.isArray(config.children)) return null
  //循环递归调用控件对应的布局元素函数
  return config.children.map((el, i) => {
    const layout = layouts[el.config.layout]
    if (layout) {
      return layout.call(this, h, el, i, config.children)
    }
    //暂时没有该控件的渲染布局元素
    return layoutIsNotFound.call(this)
  })
}


export default {
  components: {
    draggable
  },
  props: [
    //目前控件数据
    'currentItem',
    //目前序号
    'index',
    //躯干控件工程图数据
    'drawingList',
    //目前激活ID
    'activeId'
  ],
  render(h) {

    //获取当前控件对应的布局元素(需要渲染)
    const layout = layouts[this.currentItem.config.layout]

    if (layout) {
      //返回执行控件对应的布局元素函数
      return layout.call(this, h, this.currentItem, this.index, this.drawingList)
    }

    //暂时没有该控件的渲染布局元素
    return layoutIsNotFound.call(this)
  }
}
</script>
