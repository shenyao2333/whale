<template>
  <div class="icon-dialog">
    <el-dialog
      v-bind="$attrs"
      width="980px"
      :append-to-body="true"
      v-on="$listeners"
      @open="onOpen"
      @close="onClose"
    >
      <!--标题区域-搜索图标-->
      <div slot="title">
        选择图标
        <el-input
          v-model="key"
          size="mini"
          :style="{width: '260px'}"
          placeholder="请输入图标名称"
          prefix-icon="el-icon-search"
          clearable
        />
      </div>
      <!--图标展示区域-->
      <ul class="icon-ul">
        <li
          v-for="icon in iconList"
          :key="icon"
          :class="active===icon?'active-item':''"
          @click="onSelect(icon)"
        >
          <i :class="icon"/>
          <div>{{ icon }}</div>
        </li>
      </ul>
    </el-dialog>
  </div>
</template>
<script>
import iconList from '@/utils/formActivitiEngineDesign/icon.json'

//处理elementUi-icon|组装处理
const originList = iconList.map(name => `el-icon-${name}`)

export default {
  //不想继承所有父组件的内容,同时也不在组件根元素dom上显示属性
  inheritAttrs: false,
  props: ['current'],
  data() {
    return {
      //可选的图标数据
      iconList: originList,
      //目前激活中的图标
      active: null,
      //搜索图标-值
      key: ''
    }
  },
  watch: {
    //根据搜索图标->过滤图标数据
    key(val) {
      if (val) {
        this.iconList = originList.filter(name => name.indexOf(val) > -1)
      } else {
        this.iconList = originList
      }
    }
  },
  methods: {
    //打开时初始化数据
    onOpen() {
      this.active = this.current
      this.key = ''
    },
    onClose() {
    },
    //选择图标提交数据处理
    onSelect(icon) {
      this.active = icon
      this.$emit('select', icon)
      this.$emit('update:visible', false)
    }
  }
}
</script>
<style lang="scss" scoped>
//图标展示区域UI样式
.icon-ul {
  margin: 0;
  padding: 0;
  font-size: 0;
  //图标项li样式
  li {
    list-style-type: none;
    text-align: center;
    font-size: 14px;
    display: inline-block;
    width: 16.66%;
    box-sizing: border-box;
    height: 108px;
    padding: 15px 6px 6px 6px;
    cursor: pointer;
    overflow: hidden;
    //光标悬浮伪类
    &:hover {
      background: #f2f2f2;
    }

    //目前激活的图标样式
    &.active-item {
      background: #e1f3fb;
      color: #7a6df0
    }

    //图标样式
    > i {
      font-size: 30px;
      line-height: 50px;
    }
  }
}

//图标对话框样式
.icon-dialog {
  ::v-deep .el-dialog {
    border-radius: 8px;
    margin-bottom: 0;
    margin-top: 4vh !important;
    display: flex;
    flex-direction: column;
    max-height: 92vh;
    overflow: hidden;
    box-sizing: border-box;
    //对话框头部样式
    .el-dialog__header {
      padding-top: 14px;
    }

    //对话框主体样式
    .el-dialog__body {
      margin: 0 20px 20px 20px;
      padding: 0;
      overflow: auto;
    }
  }
}
</style>
