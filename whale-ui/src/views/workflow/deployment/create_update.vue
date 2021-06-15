<template>
  <el-dialog :visible.sync="dialogShow" :title="dialogTitle" @close="close">
    <el-form ref="configForm" :model="form_model" label-width="80px" label-position="left">
      <el-form-item :label="$t('flowModel.name')">
        <el-input v-model="form_model.name" :placeholder="$t('flowModel.name')"/>
      </el-form-item>
      <el-form-item :label="$t('flowModel.key')">
        <el-input v-model="form_model.key" :placeholder="$t('flowModel.key')"/>
      </el-form-item>
      <el-form-item :label="$t('flowModel.category')">
        <el-input v-model="form_model.category" :placeholder="$t('flowModel.category')"/>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="available = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>



export default {
  name: 'ModelCreateUpdate',
  props: { available: { type: Boolean, default: false }},
  data() {
    return {
      dialogTitle: '新增',
      dialogShow: this.available,
      form_model: {
        name: '',
        key: '',
        category: '',
        tenantId: this.$store.getters.name
      }
    }
  },
  watch: {
    available: function(new_v) {
      this.dialogShow = new_v
    }
  },
  methods: {
    initForm: function(data) {
      this.form_model = Object.assign({}, data)
    },
    submitForm: function() {
      var _this = this
    },
    close: function() {
      this.$emit('update:available', false)
    }
  }

}
</script>

<style scoped>

</style>
