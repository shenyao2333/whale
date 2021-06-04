<template>
  <div>
    <svg-icon icon-class="github" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'entfrmGit',
  data() {
    return {
      url: 'https://gitee.com/entfrm/entfrm-boot.git'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
