<template>
  <div>
    <el-button type="text" @click="dialogTableVisible = true">打开嵌套表格的 Dialog</el-button>

    <el-dialog :visible.sync="dialogTableVisible" title="收货地址">
      test
  </el-dialog></div>
</template>

<script>
export default {
  name: 'Userpicker',
  data() {
    return { dialogTableVisible: false }
  }
}
</script>

<style scoped>

</style>
