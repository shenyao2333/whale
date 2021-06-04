<template>
  <div class="dashboard-editor-container">

    <panel-group @handleSetLineChartData="handleSetLineChartData" />

    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>开发向导</span>
        <el-button style="float: right; padding: 3px 0" type="text">操作</el-button>
      </div>
      <div
        v-for="o in 2"
        class="item"
        :style="{backgroundColor: '#409EFF'}"
      >
        <svg-icon icon-class="star"/>
        <div class="desc">{{'向导 ' + o }}</div>
      </div>
    </el-card>

    <el-card class="box-card" style="margin-top: 15px;">
      <div slot="header" class="clearfix">
        <span>插件管理</span>
        <el-button style="float: right; padding: 3px 0" type="text">操作</el-button>
      </div>
      <div
        v-for="o in 4"
        class="item"
        :style="{backgroundColor: '#556688'}"
      >
        <svg-icon icon-class="component"/>
        <div class="desc">{{'插件 ' + o }}</div>
      </div>
    </el-card>

    <shortcut />
  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import Shortcut from './dashboard/Shortcut'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'Index',
  components: {
    PanelGroup,
    Shortcut,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart
  },
  data() {
    return {
      lineChartData: lineChartData.newVisitis
    }
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

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
</style>
