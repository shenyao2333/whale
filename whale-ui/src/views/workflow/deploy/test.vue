<template>
  <div class="containers" ref="content">
    <div class="canvas" ref="canvas">

    </div>
    <div id="js-properties-panel" class="panel">
    </div>
  </div>
</template>


<script>
// 引入相关的依赖
import BpmnViewer from 'bpmn-js'
import BpmnModeler from 'bpmn-js/lib/Modeler'
import propertiesPanelModule from 'bpmn-js-properties-panel'
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
/* 左边工具栏以及编辑节点的样式*/
import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
/* 右边工具栏样式*/
import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css'



export default {
  data(){
    return {
      // bpmn建模器
      bpmnModeler: null,
      container: null,
      canvas: null
    }
  },
  methods:{
    createNewDiagram() {
      const bpmnXmlStr = ''
      // 将字符串转换成图显示出来
      this.bpmnModeler.importXML(bpmnXmlStr, function (err) {
        if (err) {
          console.error(err);
        }
        else {
          // 这里还没用到这个，先注释掉吧
          // that.success()
        }
      })
    }
  },
  mounted(){
    // 获取到属性ref为“content”的dom节点
    this.container = this.$refs.content
    // 获取到属性ref为“canvas”的dom节点
    const canvas = this.$refs.canvas
    // 建模，官方文档这里讲的很详细
    this.bpmnModeler = new BpmnModeler({
      container: canvas,
      //添加控制板
      propertiesPanel: {
        parent: '#js-properties-panel'
      },
      additionalModules: [
        // 左边工具栏以及节点
        propertiesProviderModule,
        // 右边的工具栏
        propertiesPanelModule
      ],
      moddleExtensions: {
        camunda: camundaModdleDescriptor
      }
    });
    this.createNewDiagram(this.bpmnModeler);
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
