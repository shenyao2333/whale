<template>
  <div>
    <el-dialog v-bind="$attrs" v-on="$listeners" :close-on-click-modal="false"
               @opened="opened" @close="onClose" width="900px">

      <el-form :size="formConf.size"
               :label-position="formConf.labelPosition"
               :disabled="formConf.disabled"
               :model="form"
               :rules="rules"
               :label-width="formConf.labelWidth + 'px'">
        <component-integrated
          :drawing-list="drawingList"
          :form-data="form"
          :visible="open"/>
      </el-form>

    </el-dialog>
  </div>
</template>
<script>
import componentIntegrated from '@/components/FormActivitiEngineDesign/FormIntegrated/ComponentIntegrated'
import {formIntegrate} from "@/utils/formActivitiEngineDesign/formIntegrated";

export default {
  components: {componentIntegrated},
  props: ['drawingList', 'formConf'],
  data() {
    return {
      form: {},
      rules: {},
      open: false
    }
  },
  methods: {
    opened() {
      this.form = {}
      this.rules = {}
      formIntegrate.processIntegratedCustomizeModel(this.drawingList, true, this.form, this.rules).then(() => this.open = true)
    },
    onClose() {
      this.open = false
    }
  }
}
</script>


<style lang="scss" scoped>

</style>
