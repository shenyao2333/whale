<template>
  <div class="app-container" id="testid">
    <el-row>
      <el-col :span="24" style="text-align: center; color: #F56C6C">
        <h1>API调用次数统计</h1>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div ref="apiBar" class="apiBar" style="height:720px;" />
      </el-col>
      <el-col :span="12">
        <div ref="apiPie" class="apiPie" style="height:720px;" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { getApiStat } from "@/api/monitor/apiStat";
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme

  export default {
    data() {
      return {
        apiPie: null,
        apiBar: null
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.initChart()
      })
    },
    beforeDestroy() {
      if (!this.apiPie ) {
        return
      }
      this.apiPie.dispose()
      this.apiPie = null

      if (!this.apiBar ) {
        return
      }
      this.apiBar.dispose()
      this.apiBar = null
    },
    methods: {
      initChart() {
        this.apiPie = echarts.init(this.$refs.apiPie, 'macarons')

        this.apiBar = echarts.init(this.$refs.apiBar, 'macarons')

        getApiStat().then(response => {
          this.apiPie.setOption({
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            series: [
              {
                name: '次数',
                type: 'pie',
                roseType: 'radius',
                radius: [15, 95],
                center: ['50%', '38%'],
                data: response.data.pieSeries,
                animationEasing: 'cubicInOut',
                animationDuration: 2600
              }
            ]
          })

          this.apiBar.setOption({
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true
            },
            xAxis: {
              type: 'value',
              boundaryGap: [0, 0.01]
            },
            yAxis: {
              type: 'category',
              data: response.data.barYAxis
            },
            series: [
              {
                name: '次数',
                type: 'bar',
                data: response.data.barSeries
              }
            ]
          })
        });
      }
    }
  }
</script>
