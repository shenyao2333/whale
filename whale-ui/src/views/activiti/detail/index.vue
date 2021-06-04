<template>
  <div v-loading="loading" style="height:100%">
    <iframe :src="src" frameborder="no" id="ActivitiIframe" style="width: 100%;height: 100%" scrolling="auto"/>
  </div>
</template>
<script>
export default {
  props: {
    modelId: {
      required: true
    }
  },
  name: "Detail",
  data() {
    return {
      src: '',
      loading: true
    };
  },
  created() {
    this.src = process.env.VUE_APP_BASE_API + "/modeler.html?modelId=" + this.modelId;
  },
  mounted: function () {
    //跨域通信
    window.addEventListener('message', () => {
      this.$parent.handleClose()
    }, false)

    //检测iframe是否加载完毕
    const iframe = document.querySelector('#ActivitiIframe')
    if (iframe.attachEvent) {
      iframe.attachEvent("onload", () => {
        this.loading = false;
      });
    } else {
      iframe.onload = () => {
        this.loading = false;
      };
    }

  }
};
</script>
