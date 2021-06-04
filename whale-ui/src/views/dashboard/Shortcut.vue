<template>
  <div class="panel-group">
    <el-card class="box-card">
      <div slot="header">
        <span>快捷方式</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="toShortcut">新增</el-button>
      </div>
      <div
        v-for="shortcut in shortcutData"
        class="item"
        @click="handleJump(shortcut.path)"
        :style="{
          backgroundColor: shortcut.bgColor
        }"
      >
        <svg-icon :icon-class="shortcut.icon"/>
        <div class="desc">{{ shortcut.name }}</div>
      </div>
    </el-card>
  </div>
</template>

<script>
  import {shortcutList} from "@/api/system/shortcut";

  export default {
    data() {
      return {
        shortcutData: [
          {
            icon: 'edit',
            name: '已经反馈',
            bgColor: '#0081ff'
          },
          {
            icon: 'email',
            name: '未读邮件',
            bgColor: '#992bac'
          }
        ]
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        shortcutList({}).then(response => {
          this.shortcutData = response.data;
        });
      },
      handleJump(path) {
        this.$router.push(path)
      },
      toShortcut() {
        this.$router.push('/system/shortcut')
      }
    }
  }
</script>

<style lang="scss" scoped>
  .panel-group {
    margin-top: 10px;

    .el-card {
      margin-bottom: 16px;
    }

    /deep/ .box-card .el-card__body {
      padding: 0;
      overflow-x: auto;

      .item {
        width: 120px;
        float: left;
        margin: 10px 20px;
        color: white;
        text-align: center;
        padding: 8px;
        border-radius: 8px;
        cursor: pointer;

        svg {
          font-size: 64px;
          margin-bottom: 12px;
        }
      }
    }
  }

</style>
