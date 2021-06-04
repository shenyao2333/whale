<template>
  <!--根部组件主体元素容器-->
  <div class="container">

    <!--左面板区域-->
    <div class="left-board">
      <!--左面板上方Logo设计-->
      <div class="logo-wrapper">
        <div class="logo">
          <img :src="logo" alt="logo"> form expand designer
        </div>
      </div>
      <!--控件标本区域-有内置与自定义-->
      <el-scrollbar class="left-scrollbar">
        <div class="components-list">
          <!--渲染分类模组区域-->
          <div v-for="(item, listIndex) in leftComponents" :key="listIndex">
            <div class="components-title">
              <svg-icon icon-class="component"/>
              {{ item.title }}
            </div>
            <!--Vue拖拽请参考:http://www.itxst.com/vue-draggable/tutorial.html-->
            <draggable
              class="components-draggable"
              :list="item.list"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneComponent"
              draggable=".components-item"
              :sort="false"
              @end="onEnd"
            >
              <!--渲染系统内置控件-->
              <div v-for="(element, index) in item.list"
                   :key="index"
                   class="components-item"
                   @click="addComponent(element)">
                <div class="components-body">
                  <svg-icon :icon-class="element.config.tagIcon"/>
                  {{ element.config.label }}
                </div>
              </div>

            </draggable>
          </div>
        </div>
      </el-scrollbar>
    </div>

    <!--中心躯干面板区域-->
    <div class="center-board">
      <!--躯干上方操作区域-->
      <div class="action-bar">
        <el-button class="sava-btn" icon="el-icon-setting" type="text" @click="formCompSava">
          保存
        </el-button>
        <el-button class="preview_btn" icon="el-icon-data-line" type="text" @click="preview">
          预览
        </el-button>
        <el-button class="json_view" icon="el-icon-view" type="text" @click="showJson">
          查看json
        </el-button>
        <el-button class="clear-btn" icon="el-icon-delete" type="text" @click="empty">
          清空
        </el-button>
        <el-button class="delete-btn" icon="el-icon-circle-close" type="text" @click="closeDesigner">
          关闭
        </el-button>
      </div>
      <!--躯干控件展示区域-->
      <el-scrollbar class="center-scrollbar">
        <el-row class="center-board-row" :gutter="formConf.gutter">
          <!--核心表单布局-->
          <el-form
            :size="formConf.size"
            :label-position="formConf.labelPosition"
            :disabled="formConf.disabled"
            :label-width="formConf.labelWidth + 'px'"
          >
            <!--Vue拖拽请参考:http://www.itxst.com/vue-draggable/tutorial.html-->
            <draggable class="drawing-board" :list="drawingList" :animation="340" group="componentsGroup">
              <draggable-item
                v-for="(item, index) in drawingList"
                :key="item.config.renderKey"
                :drawing-list="drawingList"
                :current-item="item"
                :index="index"
                :active-id="activeId"
                @activeItem="activeFormItem"
                @copyItem="drawingItemCopy"
                @deleteItem="drawingItemDelete"
              />
            </draggable>
            <div v-show="!drawingList.length" class="empty-info">
              从左侧拖入或点选组件进行表单扩展设计
            </div>
          </el-form>
        </el-row>
      </el-scrollbar>
    </div>

    <!--右面板区域-->
    <right-panel
      ref="rightPanel"
      :active-data="activeData"
      :show-field="!!drawingList.length"
      :data-conf="dataConf"
      :id-global="activeId"
      :form-extend-id="formExtendId"
      :field-length-min="fieldLengthMin"
      :option="option"
      @tag-change="tagChange"
    />

    <!--预览抽屉-->
    <form-drawer
      :title="title"
      :append-to-body="true"
      :visible.sync="drawerVisible"
      :form-data="formData"
      :form-conf="formConf"
      :drawing-list="drawingList"
      size="100%"
    />

    <!--查看json抽屉-->
    <json-drawer
      :append-to-body="true"
      size="60%"
      :visible.sync="jsonDrawerVisible"
      :json-str="JSON.stringify(formData)"
    />

  </div>
</template>

<script>
import draggable from 'vuedraggable'
import {debounce} from 'throttle-debounce'
import FormDrawer from './DesignBuild/FormDrawer'
import JsonDrawer from './DesignBuild/JsonDrawer'
import RightPanel from './DesignBuild/RightPanel'
import {inputComponents, selectComponents, layoutComponents, formConf, dataConf} from './DesignConfig/config'
import {deepClone, comparePadding} from '@/utils/formExpandDesign/index'
import logo from '@/assets/formExpandDesign/logo.png'
import DraggableItem from './DesignBuild/DraggableItem'
import {
  getDrawingList,
  saveDrawingList,
  getIdGlobal,
  saveIdGlobal,
  saveTreeNodeData,
  getTreeNodeData,
  getDataConf,
  savaDataConf
} from '@/utils/formExpandDesign/db'
import {addFormExtendDesign, editFormExtendDesign} from '@/api/intelligent/formExtendDesign'
import {getFormExtendCascade} from '@/api/intelligent/formExtend'
import {
  savaAddedOption,
  savaRemovedOption,
  clearOptionPool,
  getChanges,
  addedPool,
  modifiedPool,
  removedPool
} from '@/utils/formExpandDesign/optionPool'
import validate from '@/utils/formExpandDesign/validate'

export default {
  components: {
    draggable,
    FormDrawer,
    JsonDrawer,
    RightPanel,
    DraggableItem
  },
  props: {
    formExtendId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      //预览标题
      title: "entfrm开发平台",
      //增改标识[0:新增,1:修改]
      option: 0,
      //logo图标
      logo,
      //挂载组件完毕制定字段最小值
      fieldLengthMin: 0,
      //记录目前控件全局ID-自增长
      idGlobal: 100,
      //表单属性【右面板】-静态配置
      formConf,
      //数据配置【右面板】
      dataConf: deepClone(dataConf),
      //分类模组-输入类型【左面板】
      inputComponents,
      //分类模组-输入类型【左面板】
      selectComponents,
      //分类模组-输入类型【左面板】
      layoutComponents,
      //躯干控件工程图
      drawingList: [],
      //目前激活控件ID
      activeId: 100,
      //抽屉可见
      drawerVisible: false,
      //表单数据
      formData: {},
      //查看json抽屉可见
      jsonDrawerVisible: false,
      //目前激活控件数据
      activeData: {},
      //函数防抖-缓存目前躯干控件工程图
      saveDrawingListDebounce: debounce(340, saveDrawingList),
      //函数防抖-缓存目前控件全局ID-避免db读取冲突
      saveIdGlobalDebounce: debounce(340, saveIdGlobal),
      //函数防抖-缓存数据配置
      saveDataConfDebounce: debounce(340, savaDataConf),
      //分类模组集合数据
      leftComponents: [
        {
          title: '输入型组件',
          list: inputComponents
        },
        {
          title: '选择型组件',
          list: selectComponents
        },
        {
          title: '布局型组件',
          list: layoutComponents
        }
      ],
      /////////////////////////////////////////////数据逻辑操作属性
      //自动化定位占位提示属性校验状态
      automaticPositioningState: undefined,
      //旧激活ID(组件一致处理)
      oldActiveId: 100,
      //临时保存拖拽控件数据
      tempActiveData: {},
      //初始化躯干控件工程图(数据读取)
      drawingListInDB: getDrawingList(),
      //初始化全局ID(数据读取)
      idGlobalDB: getIdGlobal(),
      //初始化数据配置(数据读取)
      dataConfDB: getDataConf()
    }
  },
  computed: {},
  watch: {
    //标题发生变化->修改占位提示
    'activeData.config.label': function (val, oldVal) {
      if (this.activeData.attrs.placeholder === undefined || !this.activeData.config.tag ||
        this.oldActiveId !== this.activeId) return
      if (!this.automaticPositioningState) this.activeData.attrs.placeholder = this.activeData.attrs.placeholder.replace(oldVal, '') + val
      this.automaticPositioningState = undefined
    },
    //更换激活控件时->目前激活ID赋值给旧激活ID
    activeId: {
      handler(val) {
        this.oldActiveId = val
      },
      immediate: true
    },
    //躯干控件工程图数据发生变化->缓存躯干控件工程图
    drawingList: {
      handler(val) {
        if (this.option === 0) {
          this.drawingListInDB[this.formExtendId] = val
          this.saveDrawingListDebounce(this.drawingListInDB)
        }
        if (val.length === 0) {
          this.idGlobal = 100
          if (this.option === 1) {
            this.$store.dispatch("treeNodeData/setFormExtendTreeNodeData", '')
          } else {
            let fetchTreeNode = getTreeNodeData()
            fetchTreeNode[this.formExtendId] = {}
            saveTreeNodeData(fetchTreeNode)
          }
        }
      },
      deep: true
    },
    //添加控件时->缓存目前控件全局ID
    idGlobal: {
      handler(val) {
        if (this.option === 0) {
          this.idGlobalDB[this.formExtendId] = val
          this.saveIdGlobalDebounce(this.idGlobalDB)
        }
      }
    },
    //数据配置修改时进行缓存数据
    dataConf: {
      handler(val) {
        if (this.option === 0) {
          this.dataConfDB[this.formExtendId] = val
          this.saveDataConfDebounce(this.dataConfDB)
        }
      },
      deep: true
    }
  },
  mounted() {
    //数据库中是否有躯干控件工程图数据
    getFormExtendCascade(this.formExtendId).then(response => {
      if (!!response.data) {
        //读取控件全局ID
        this.idGlobal = response.data.idGlobal
        //读取DB躯干控件工程图数据-渲染至躯干局域
        this.drawingList = JSON.parse(response.data.drawingControls)
        this.dataConf = response.data.dataConf
        this.$store.dispatch("treeNodeData/setFormExtendTreeNodeData", response.data.treeNodeId)
        this.option = 1
        //挂载组件完毕制定字段最小值
        this.fieldLengthMin = this.dataConf.fieldLength
      } else {
        //读取控件全局ID
        if (this.idGlobalDB[this.formExtendId]) this.idGlobal = this.idGlobalDB[this.formExtendId]
        //读取DB躯干控件工程图数据-渲染至躯干局域
        if (Array.isArray(this.drawingListInDB[this.formExtendId]) && this.drawingListInDB[this.formExtendId].length > 0)
          this.drawingList = this.drawingListInDB[this.formExtendId]
        //读取数据配置数据
        if (this.dataConfDB[this.formExtendId]) this.dataConf = this.dataConfDB[this.formExtendId]
        this.option = 0
      }
      //设置激活控件(第一个)
      if (this.drawingList[0]) this.activeFormItem(this.drawingList[0])
      this.$nextTick(() => clearOptionPool())
      window.addedPool = addedPool
      window.modifiedPool = modifiedPool
      window.removedPool = removedPool
    })
  },
  methods: {
    //设置激活控件
    activeFormItem(currentItem) {
      this.activeData = currentItem
      this.activeId = currentItem.config.formId
    },
    //拖拽完成设置激活目前控件
    onEnd(obj) {
      if (obj.from !== obj.to) {
        this.activeData = this.tempActiveData
        savaAddedOption(this.tempActiveData)
        console.info("保存新增数据", this.tempActiveData)
        this.activeId = this.idGlobal
      }
    },
    //控件发送点击添加控件到躯干控件工程图
    addComponent(item) {
      const clone = this.cloneComponent(item)
      this.drawingList.push(clone)
      savaAddedOption(clone)
      console.info("保存新增数据", clone)
      this.activeFormItem(clone)
    },
    //克隆控件-保证数据独立
    cloneComponent(origin) {
      const clone = deepClone(origin)
      const config = clone.config
      this.createIdAndKey(clone)
      clone.attrs.placeholder !== undefined && (clone.attrs.placeholder += config.label)
      this.tempActiveData = clone
      return this.tempActiveData
    },
    //递归创建组件全局ID根Key[全局信息注册]
    createIdAndKey(item) {
      const config = item.config
      config.formId = ++this.idGlobal
      config.renderKey = `${config.formId}${+new Date()}`
      if (config.layout === 'colFormItem' && !config.vModelNotRequire) {
        config.vModel = `field${this.idGlobal}`
      } else if (config.layout === 'rowFormItem') {
        config.componentName = `row${this.idGlobal}`
        !Array.isArray(config.children) && (config.children = [])
        //rowFormItem无需配置label属性
        delete config.label
      }
      if (Array.isArray(config.children)) {
        config.children = config.children.map(childItem => this.createIdAndKey(childItem))
      }
      return item
    },
    //组装表单JSON查看数据[躯干+操作记录]
    AssembleFormData() {
      this.formData = {
        drawingControls: deepClone(this.drawingList),
        dataConf: deepClone(this.dataConf)
      }
    },
    //组装表单保存数据[躯干+操作记录+数据配置]
    AssembleFormSavaData() {
      this.formData = {
        drawingControls: deepClone(this.drawingList),
        dataConf: deepClone(this.dataConf),
        optionPool: getChanges()
      }
    },
    //清空躯干局域所有组件
    empty() {
      this.$confirm('确定要清空所有组件吗？', '提示', {type: 'warning'}).then(
        () => {
          savaRemovedOption(this.drawingList)
          console.info("保存移除数据", this.drawingList)
          this.initializationData()
        }
      )
    },
    //还原数据
    initializationData() {
      this.drawingList = []
      this.idGlobal = 100
      if (this.option === 1) {
        this.$store.dispatch("treeNodeData/setFormExtendTreeNodeData", '')
      } else {
        let fetchTreeNode = getTreeNodeData()
        fetchTreeNode[this.formExtendId] = {}
        saveTreeNodeData(fetchTreeNode)
      }
    },
    //复制控件
    drawingItemCopy(item, list) {
      let clone = deepClone(item)
      clone = this.createIdAndKey(clone)
      list.push(clone)
      savaAddedOption(clone)
      console.info("保存新增数据", clone)
      this.activeFormItem(clone)
    },
    //级联组件清除目前单次缓存
    clearCascaderCache(currentItem) {
      if (this.vGet(currentItem, 'config.tag') === 'el-cascader' && this.option === 1) {
        currentItem.config.options = []
        let temp = this.$store.state.treeNodeData.formExtendTreeNodeData
        if (temp) {
          temp = JSON.parse(temp)
          delete temp[this.vGet(currentItem, 'config.formId')]
          this.$store.dispatch("treeNodeData/setFormExtendTreeNodeData", temp)
        }
      }
      if (this.vGet(currentItem, 'config.tag') === 'el-cascader' && this.option === 0) {
        currentItem.config.options = []
        let fetchTreeNode = getTreeNodeData()
        let child = fetchTreeNode[this.formExtendId]
        if (child && Object.keys(child).length > 0) {
          delete child[this.vGet(currentItem, 'config.formId')]
          fetchTreeNode[this.formExtendId] = child
          saveTreeNodeData(fetchTreeNode)
        }
      }
    },
    //删除控件
    drawingItemDelete(index, list) {
      //缓存实时同步,级联选择组件
      this.clearCascaderCache(list[index])
      //移除操作
      savaRemovedOption(this.drawingList[index])
      console.info("保存移除数据", this.drawingList[index])
      list.splice(index, 1)
      this.$nextTick(() => {
        const len = this.drawingList.length
        if (len) {
          if (this.drawingList[index - 1]) {
            this.activeFormItem(this.drawingList[index - 1])
          } else {
            this.activeFormItem(this.drawingList[index])
          }
        }
      })
    },
    //打开查看json抽屉
    showJson() {
      this.AssembleFormData()
      this.jsonDrawerVisible = true
    },
    //修改目前激活控件类型
    tagChange(newTag) {
      newTag = this.cloneComponent(newTag)
      const config = newTag.config
      //缓存实时同步,级联选择组件
      this.clearCascaderCache(this.activeData)
      savaRemovedOption(this.activeData, true)
      console.info("保存移除数据", this.activeData)
      ////////////////////////////////////////////
      this.activeData.config.tag = config.tag
      this.activeData.config.tagIcon = config.tagIcon
      this.activeData.config.document = config.document
      comparePadding(newTag, this.activeData)
      ////////////////////////////////////////////
      savaAddedOption(newTag)
      console.info("保存新增数据", newTag)
      this.activeData = newTag
      this.updateDrawingList(newTag, this.drawingList)
    },
    //修改躯干控件工程图目标控件
    updateDrawingList(newTag, list) {
      const index = list.findIndex(item => item.config.formId === this.activeId)
      if (index > -1) {
        list.splice(index, 1, newTag)
      } else {
        list.forEach(item => {
          if (Array.isArray(item.config.children)) this.updateDrawingList(newTag, item.config.children)
        })
      }
    },
    closeDesigner() {
      this.$parent.handleClose()
    },
    //表单预览
    preview() {
      this.drawerVisible = true
    },
    //表单控件[保存-修改]
    formCompSava() {
      const loading = this.$loading({
        lock: true,
        customClass: 'create-isLoading',  // *这里设置他的class名称,这里最重要
        text: '请稍等,保存中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0)'
      })
      this.$refs["rightPanel"].$refs["dataConfigForm"].validate(valid => {
        if (valid) {
          this.AssembleFormSavaData()
          validate.designValidate({
            formExtendId: this.formExtendId,
            jsonMode: this.formData,
            idGlobal: this.idGlobal,
            treeNodeId: this.$store.state.treeNodeData.formExtendTreeNodeData
          }, this.option).then((state, message, dataModel, activeId, currentTab) => {
            if (state) {
              switch (this.option) {
                case 0 :
                  addFormExtendDesign(dataModel).then(response => {
                    if (response.code === 0) {
                      this.msgSuccess("保存成功")
                      this.fastCacheClear()
                      clearOptionPool()
                      this.closeDesigner()
                      loading.close();
                    } else {
                      this.msgError(response.msg)
                      loading.close();
                    }
                  })
                  break
                case 1 :
                  editFormExtendDesign(dataModel).then(response => {
                    if (response.code === 0) {
                      this.msgSuccess("保存成功")
                      this.fastCacheClear()
                      clearOptionPool()
                      this.closeDesigner()
                      loading.close();
                    } else {
                      this.msgError(response.msg)
                      loading.close();
                    }
                  })
                  break
              }
            } else {
              this.msgError(message);
              this.$refs.rightPanel.currentTab = currentTab
              if (activeId) {
                this.activeId = activeId
                setTimeout(() => {
                  this.automaticPositioningState = true
                  document.getElementsByClassName('active-from-item')[0].click()
                }, 300)
              }
              loading.close();
            }
          })
        } else {
          this.$refs.rightPanel.currentTab = validate.rightTab[1]
          loading.close();
        }
      });
    },
    //立即清除缓存数据
    fastCacheClear() {
      let fetchDrawingList = getDrawingList(), fetchIdGlobal = getIdGlobal(), fetchDataConf = getDataConf()
      fetchDrawingList[this.formExtendId] = []
      fetchIdGlobal[this.formExtendId] = 100
      fetchDataConf[this.formExtendId] = {}
      saveDrawingList(fetchDrawingList)
      saveIdGlobal(fetchIdGlobal)
      savaDataConf(fetchDataConf)
      if (this.option === 1) {
        this.$store.dispatch("treeNodeData/setFormExtendTreeNodeData", '')
      } else {
        let fetchTreeNode = getTreeNodeData()
        fetchTreeNode[this.formExtendId] = {}
        saveTreeNodeData(fetchTreeNode)
      }
    }
  }
}
</script>

<style lang='scss'>
@import '~@/assets/styles/formExpandDesign';

.create-isLoading {
  .el-loading-spinner {
    top: 50%;
    left: 50%;
    margin-left: -55px;
    background: rgba(0, 0, 0, 0.5);
    padding: 20px;
    border-radius: 4px;
    width: auto;
    text-align: center;
    position: absolute;

    i {
      color: #eee;
    }

    .el-loading-text {
      color: #eee;
    }
  }
}

</style>
