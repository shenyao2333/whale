<template>
  <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
    <el-menu-item :index="app.id + ''" :key="app.id" v-for="app in apps">{{app.name}}</el-menu-item>
  </el-menu>
</template>

<script>

  export default {
    computed: {
      apps() {
        return this.$store.state.permission.apps
      },
      activeIndex() {
        var aindex = '1'
        if (this.$store.state.permission.apps && this.$store.state.permission.apps.length > 0) {
          aindex = this.$store.state.permission.apps[0].id + ''
        }
        this.activeRoutes(aindex, true)
        return aindex
      }
    },
    methods: {
      handleSelect(key, keyPath) {
        this.activeRoutes(key, false)
      },
      activeRoutes(index, first){
        var aRoutes =[]
        if(this.$store.state.permission.routes && this.$store.state.permission.routes.length >0){
          this.$store.state.permission.routes.map(item =>{
            if(index == item.appId || (first && item.appId == '0') || (!first && item.appId == '0' && index == this.$store.state.permission.apps[0].id + '')){
              aRoutes.push(item)
            }
          })
        }
        this.$store.commit('ACTIVE_ROUTES', aRoutes)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .el-menu--horizontal > .el-menu-item {
    float: left;
    height: 50px;
    line-height: 50px;
    margin: 0;
    border-bottom: 3px solid transparent;
    color: #909399;
    padding: 0 10px;
    margin: 0 10px;
  }

  .el-menu--horizontal > .el-menu-item.is-active {
    border-bottom: 3px solid #64D9D6;
    color: #303133;
  }
</style>
