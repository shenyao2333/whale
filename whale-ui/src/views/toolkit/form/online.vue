<template>
  <div class="online">
    <avue-form
      ref="form"
      class="online-form"
      :option="formData"
      v-model="formModels"
      @submit="handleSubmit"
    ></avue-form>
  </div>
</template>
<script>
import {getForm, addDynamicForm} from "@/api/toolkit/form";
export default {
  name: "Online",
  data() {
    return {
      formData: {},
      formModels: {},
    }
  },
  created() {
    this.getFormInfo();
  },
  methods: {
    getFormInfo(){
      getForm(this.$route.query.id).then(response => {
        this.formData = JSON.parse(response.data.data);
      });
    },
    handleSubmit(form, done) {
      if (done) {
        const formId = this.$route.query.id
        console.log(formId)
        addDynamicForm({formId: formId, data: JSON.stringify(this.formModels)}).then(response => {
          this.msgSuccess("提交成功");
          done()
        });
      }
    },
  },
}
</script>
<style lang="scss" scoped>
.online {
  width: 1200px;
  margin: 30px auto;

  .online-form {
    overflow-y: hidden;
  }
}

</style>
