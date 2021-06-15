<template>
  <div ref="content" class="containers">
    <div ref="canvas" class="canvas"/>
    <div :class="{hide:!pannelShow}" class="panel" >
      <div class="panelBtn" @click="hoverPanel">
        <div style="margin-top: 9px">
        {{ pannelShow?'隐藏面板':'显示面板' }}
        </div>
      </div>
      <div id="js-properties-panel" ref="jsPropertiesPanel" class="panel"/>
    </div>

    <el-button-group>
      <el-button type="primary" size="mini" icon="el-icon-circle-check" circle @click="exportXML"/>
      <el-button type="warning" size="mini" icon="el-icon-circle-close-outline" circle @click="close()"/>
    </el-button-group>
  </div>
</template>

<script>
// 引入相关的依赖
/* import BpmnViewer from 'bpmn-js'*/``
import BpmnModeler from 'bpmn-js/lib/Modeler'
import propertiesPanelModule from './lib'

//import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'

import propertiesProviderModule from './lib/provider/camunda'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
/* 左边工具栏以及编辑节点的样式*/
import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
/* 右边工具栏样式*/
import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css'
import customTranslate from './customTranslate'

export default {
  name: 'BpmnDrawer',
  data() {
    return {
      // bpmn建模器
      bpmnModeler: null,
      container: null,
      canvas: null,
      modelId: '',
      deployName: '',
      pannelShow: true
    }
  },
  mounted() {
    propertiesPanelModule.propertiesPanel.assigneeClick = function() {
      alert(1)
    }

    // 获取到属性ref为“content”的dom节点
    this.container = this.$refs.content
    this.jsPropertiesPanel = this.$refs.jsPropertiesPanel
    // 获取到属性ref为“canvas”的dom节点
    const canvas = this.$refs.canvas

    // 建模，官方文档这里讲的很详细
    this.bpmnModeler = new BpmnModeler({
      container: canvas,
      // 添加控制板
      propertiesPanel: {
        parent: '#js-properties-panel'
      },
      additionalModules: [
        // 左边工具栏以及节点
        propertiesProviderModule,
        // 右边的工具栏
        propertiesPanelModule,
        {
          translate: ['value', customTranslate]
        }
      ],
      moddleExtensions: {
        camunda: camundaModdleDescriptor
      }
    })
  },
  methods: {
    hoverPanel: function() {
      this.pannelShow = !this.pannelShow
    },
    close: function() {
      this.$emit('close')
    },
    selectUser: function() {
      alert(1)
    },
    initCanvas: function(xml, modelId, name) {
      this.modelId = modelId
      this.deployName = name
      var _this = this
      // 将字符串转换成图显示出来
      this.bpmnModeler.importXML(xml, function(err) {
        if (err) {
          console.error(err)
        } else {
          _this.$notify({
            title: '成功',
            message: '渲染成功',
            type: 'success',
            duration: 2000
          })
        }
      })
    },
    exportXML: function() {
      var _this = this
      this.bpmnModeler.saveXML({ format: true }, function(err, xml) {
        if (err) {
          return console.error('could not save BPMN 2.0 diagram', err)
        }
        if (_this.deployName !== '' && _this.deployName !== undefined && _this.deployName !== null) {
          _this.$emit('exportXML', xml, _this.deployName, _this.modelId)
          return
        }
        _this.$prompt('部署名称', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: function(val) {
            if (val === null || val === '' || val === undefined) {
              return '请输出部署名称'
            }
            return true
          },
          inputErrorMessage: '请输出部署名称'
        }).then(({ value }) => {
          _this.$emit('exportXML', xml, value +
            '.bpmn20.xml', _this.modelId)
        }).catch(() => {
          _this.$message({
            type: 'info',
            message: '取消输入'
          })
        })
      })
    },
    dovalicate: function() {
      var camundaId = document.getElementById('camunda-id').value
      if (camundaId === '') {
        this.$notify({
          title: '校验失败',
          message: '请输入流程id',
          type: 'warning',
          duration: 2000
        })
        return false
      }
      return true
    }
  }
}
</script>

<style lang="scss">
.containers {
  position: absolute;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
}

.canvas {
  width: 100%;
  height: 100%;
}

.panel {
  position: absolute;
  right: 0;
  top: 0;
  width: 300px;
  overflow-y: auto;
  height: 600px;
}
.panel.hide {
  width: 30px;
}
.bjs-powered-by{
  display: none;
}
.el-button-group{
  position: absolute;
  top: 1px;
  left: 80px;
}
.panelBtn{
  position: absolute;
  z-index: 102;
  top: 50%;
  left: 0px;
  overflow: visible;
  width: 10px;
  height: 60px;
  background-color: #909399;
  font-size: 8px;
  border-radius: 22px 4px;
  cursor: pointer;
  color: #fff;
}
</style>
