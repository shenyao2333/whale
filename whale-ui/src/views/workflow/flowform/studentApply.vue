<template>
  <div >
    <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px" class="demo-ruleForm" >
      <el-form-item label="学校名称" prop="name">
        <el-select v-model="ruleForm.schoolname" placeholder="请选择学校名称">
          <el-option label="上海交通大学" value="shanghai"/>
          <el-option label="南京大学" value="nanjing"/>
        </el-select>
      </el-form-item>
      <el-form-item label="年级" prop="region">
        <el-input v-model="ruleForm.region"/>
      </el-form-item>
      <el-form-item label="报名性质">
        <el-checkbox-group v-model="ruleForm.bmtype">
          <el-checkbox label="美食/餐厅线上活动" name="type"/>
          <el-checkbox label="地推活动" name="type"/>
          <el-checkbox label="线下主题活动" name="type"/>
          <el-checkbox label="单纯品牌曝光" name="type"/>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="报名类型">
        <el-radio-group v-model="ruleForm.resource">
          <el-radio label="补考"/>
          <el-radio label="选课"/>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="报名备注">
        <el-input v-model="ruleForm.descript" type="textarea"/>
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" @click="submitForm('ruleForm')">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>

export default {
  data() {
    return {
      loading: false,
      ruleForm: {
        schoolname: '',
        region: '',
        dateday: '',
        datetime: '',
        delivery: false,
        bmtype: [],
        resource: '',
        descript: ''
      },
      rules: {
        schoolname: [
          { required: true, message: '请选择学校名称', trigger: 'change' }

        ],
        region: [
          { required: true, message: '请输入年级', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        dateday: [
          { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
        ],
        datetime: [
          { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
        ],
        bmtype: [
          { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
        ],
        resource: [
          { required: true, message: '请选择活动资源', trigger: 'change' }
        ],
        descript: [
          { required: true, message: '请填写活动形式', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>
