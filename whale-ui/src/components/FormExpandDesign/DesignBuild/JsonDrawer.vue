<template>
  <div>
    <el-drawer v-bind="$attrs" v-on="$listeners" @opened="onOpen" @close="onClose">
      <!--查看Json头部区域-->
      <div class="action-bar" :style="{'text-align': 'left'}">
        <span ref="copyBtn" class="bar-btn copy-json-btn">
          <i class="el-icon-document-copy"/>
          复制JSON
        </span>
        <span class="bar-btn" @click="exportJsonFile">
          <i class="el-icon-download"/>
          导出JSON文件
        </span>
        <span class="bar-btn delete-btn" @click="$emit('update:visible', false)">
          <i class="el-icon-circle-close"/>
          关闭
        </span>
      </div>
      <!--摩纳哥编辑器主体区域-->
      <div id="editorJson" class="json-editor"/>
    </el-drawer>
  </div>
</template>

<script>
import {beautifierConf} from '@/utils/formExpandDesign/index'
import ClipboardJS from 'clipboard'
import {saveAs} from 'file-saver'
import beautifier from 'js-beautify'
import * as monaco from 'monaco-editor';

export default {
  components: {},
  props: {
    jsonStr: {
      type: String,
      required: true
    }
  },
  data() {
    return {}
  },
  computed: {},
  watch: {},
  created() {
  },
  mounted() {
    //设置剪贴板事件处理
    const clipboard = new ClipboardJS('.copy-json-btn', {
      text: trigger => {
        this.$notify({
          title: '成功',
          message: '代码已复制到剪切板，可粘贴。',
          type: 'success'
        })
        return this.beautifierJson
      }
    })
    clipboard.on('error', e => {
      this.$message.error('代码复制失败')
    })
  },
  methods: {
    //初始化json数据
    onOpen() {
      this.beautifierJson = beautifier.js(this.jsonStr, beautifierConf.js)
      this.setEditorValue('editorJson', this.beautifierJson)
    },
    onClose() {
    },
    //设置摩纳哥编辑器|初始化|值
    setEditorValue(id, codeStr) {
      if (this.jsonEditor) {
        this.jsonEditor.setValue(codeStr)
      } else {
        this.jsonEditor = monaco.editor.create(document.getElementById(id), {
          value: codeStr,
          theme: 'vs-dark',
          language: 'json',
          automaticLayout: true,
          readOnly: true
        })
      }
    },
    //导出Json文件-Blob
    exportJsonFile() {
      this.$prompt('文件名:', '导出文件', {
        inputValue: `${+new Date()}.json`,
        closeOnClickModal: false,
        inputPlaceholder: '请输入文件名'
      }).then(({value}) => {
        if (!value) value = `${+new Date()}.json`
        const codeStr = this.jsonEditor.getValue()
        const blob = new Blob([codeStr], {type: 'text/plain;charset=utf-8'})
        saveAs(blob, value)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '~@/assets/styles/formExpandDesign/mixin.scss';
//递归查找隐藏抽屉标题
::v-deep .el-drawer__header {
  display: none;
}

//引入混入样式
@include action-bar;
//设置摩纳哥编辑器主体区域样式
.json-editor {
  height: calc(100vh - 33px);
}
</style>
