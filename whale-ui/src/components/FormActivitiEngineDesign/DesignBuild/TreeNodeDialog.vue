<template>
  <div>
    <!--添加树形节点对话框-->
    <el-dialog
      v-bind="$attrs"
      :close-on-click-modal="false"
      :append-to-body="true"
      v-on="$listeners"
      @open="onOpen"
    >
      <!--数据表单-->
      <el-row :gutter="0">
        <el-form
          ref="elForm"
          :model="formData"
          :rules="rules"
          size="small"
          label-width="100px"
        >
          <!--选项名-->
          <el-col :span="24">
            <el-form-item
              label="选项名"
              prop="label"
            >
              <el-input
                v-model="formData.label"
                placeholder="请输入选项名"
                clearable
              />
            </el-form-item>
          </el-col>
          <!--选项值-->
          <el-col :span="24">
            <el-form-item
              label="选项值"
              prop="value"
            >
              <el-input
                v-model="formData.value"
                placeholder="请输入选项值"
                clearable
              >
                <template slot="append">字符串</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <!--按钮区域-->
      <div slot="footer">
        <el-button
          type="primary"
          @click="handelConfirm"
        >
          确定
        </el-button>
        <el-button @click="onClose">
          取消
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {deepClone} from "@/utils/formActivitiEngineDesign";

export default {
  components: {},
  //不希望组件的根元素继承特性(没有在props声明的属性都会添加到组件根元素)
  inheritAttrs: false,
  props: {
    idGlobal: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      //记录目前节点局部ID-自增长
      id: 100,
      //局域[Tree-Key]ID数据
      treeNodeData: {},
      //表单数据
      formData: {
        label: undefined,
        value: undefined
      },
      //表单校验规则
      rules: {
        label: [
          {
            required: true,
            message: '请输入选项名',
            trigger: 'blur'
          }
        ],
        value: [
          {
            required: true,
            message: '请输入选项值',
            trigger: 'blur'
          }
        ]
      },
      //初始化节点Keys数据(数据读取)
      treeNodeDataDB: {}
    }
  },
  computed: {},
  watch: {
    //目前节点Keys数据发生变化->持久化目前节点Keys数据
    treeNodeData: {
      handler(val) {
        this.$store.dispatch("treeNodeData/setFormActivitiEngineTreeNodeData", val)
      },
      deep: true
    },
    //触发-持久化目前节点Keys数据监控
    id(val) {
      this.treeNodeData = Object.assign(deepClone(this.treeNodeData), {[this.idGlobal]: val})
    }
  },
  methods: {
    //对话框打开时初始化数据
    onOpen() {
      //初始化节点ID
      let temp = this.$store.state.treeNodeData.formActivitiEngineTreeNodeData
      if (temp) {
        this.treeNodeData = JSON.parse(temp)
        this.id = this.treeNodeData[this.idGlobal] || 100
      }
      this.formData = {
        label: undefined,
        value: undefined
      }
    },
    onClose() {
      this.$emit('update:visible', false)
    },
    //确认提交当前添加的节点处理
    handelConfirm() {
      this.$refs.elForm.validate(valid => {
        if (!valid) return
        this.formData.value = String(this.formData.value)
        this.formData.id = ++this.id
        this.$emit('commit', this.formData)
        this.onClose()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
