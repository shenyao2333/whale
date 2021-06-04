<template>
  <div v-if="visible">
    <el-row :gutter="formConf.gutter">
      <build-form-item
        v-for="item in drawingControlsList"
        :current-item="item"
        :key="item.config.renderKey"
      />
    </el-row>
  </div>
</template>

<script>
import buildFormItem from "./BuildFormItem";
import {formIntegrate} from '@/utils/formActivitiEngineDesign/formIntegrated'
import {formConf} from '@/components/FormActivitiEngineDesign/DesignConfig/config'
import {deepClone} from "@/utils";


/**
 *<p>
 * Form表单·动态构建混入组件
 * 采用JSX渲染
 *</p>
 *
 * @Author: entfrm开发团队-王翔
 * @Date: 2021/3/23
 */
export default {
  name: "componentIntegrated",
  components: {buildFormItem},
  props: {
    formCode: String,
    formData: {
      type: Object,
      required: true
    },
    visible: {
      type: Boolean,
      default: false
    },
    drawingList: Array
  },
  data() {
    return {
      formConf,
      drawingControlsList: [],
    }
  },
  watch: {
    visible: {
      handler(val) {
        if (val) {
          this.onOpen()
        } else {
          this.onClose()
        }
      },
      immediate: true
    }
  },
  computed: {},
  methods: {
    onOpen() {
      if (Array.isArray(this.drawingList)) {
        formIntegrate.processIntegratedCustomizeModel(deepClone(this.drawingList), false, this.formData).then(e => this.drawingControlsList = e)
      } else {
        formIntegrate.processIntegratedModel(false, this.formCode, this.formData).then(e => this.drawingControlsList = e)
      }
    },
    onClose() {
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">

</style>
