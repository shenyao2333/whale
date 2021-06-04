<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header"><span>基本信息</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <tbody>
                <tr>
                  <td><div class="cell">Redis版本</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.redis_version }}</div></td>
                  <td><div class="cell">运行模式</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.redis_mode }}</div></td>
                  <td><div class="cell">监听端口</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.tcp_port }}</div></td>
                  <td><div class="cell">已连客户端</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.connected_clients }}</div></td>
                </tr>
                <tr>
                  <td><div class="cell">启动秒数</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.uptime_in_seconds }}</div></td>
                  <td><div class="cell">启动天数</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.uptime_in_days }}</div></td>
                  <td><div class="cell">内存限制</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.maxmemory }}</div></td>
                  <td><div class="cell">系统内存</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.maxmemory_human }}</div></td>
                </tr>
                <tr>
                  <td><div class="cell">是否启动AOF</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.aof_enabled }}</div></td>
                  <td><div class="cell">RDB是否成功</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.rdb_last_bgsave_status }}</div></td>
                  <td><div class="cell">Key数量</div></td>
                  <td><div class="cell" v-if="redis.dbSize">{{ redis.dbSize }}</div></td>
                  <td><div class="cell">网络入口/出口</div></td>
                  <td><div class="cell" v-if="redis.info">{{ redis.info.instantaneous_input_kbps }}kps/{{ redis.info.instantaneous_output_kbps }}kps</div></td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header"><span>命令统计</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="commandPie" class="commandPie" style="height:420px;" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span>内存信息</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="guageDb" class="guageDb" style="height:420px;" />
          </div>
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>

<script>
import { getRedis } from "@/api/monitor/redis";
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme

export default {
  name: "Server",
  data() {
    return {
      // 加载层信息
      loading: [],
      commandPie: null,
      guageDb: null,
      // redis信息
      redis: []
    };
  },
  beforeDestroy() {
    if (!this.commandPie ) {
      return
    }
    this.commandPie.dispose()
    this.commandPie = null
  },
  created() {
    this.getList();
    this.openLoading();
  },
  methods: {
    /** 查询redis信息 */
    getList() {
      getRedis().then(response => {
        this.redis = response.data;
        this.loading.close();

        this.commandPie = echarts.init(this.$refs.commandPie, 'macarons')
        this.commandPie.setOption({
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          series: [
            {
              name: '命令',
              type: 'pie',
              roseType: 'radius',
              radius: [15, 95],
              center: ['50%', '38%'],
              data: response.data.commandStats,
              animationEasing: 'cubicInOut',
              animationDuration: 2600
            }
          ]
        })
        this.guageDb = echarts.init(this.$refs.guageDb, 'macarons')
        this.guageDb.setOption({
          tooltip: {
            formatter: "{b} <br/>{a} : {c}M"
          },
          series: [
            {
              name: "峰值",
              type: "gauge",
              min: 0,
              max: 1000,
              detail: {
                formatter: "{value}M"
              },
              data: [
                {
                  value: parseFloat(this.redis.info.used_memory_human),
                  name: "内存消耗"
                }
              ]
            }
          ]
        })

      });
    },
    // 打开加载层
    openLoading() {
      this.loading = this.$loading({
        lock: true,
        text: "拼命读取中",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    }
  }
};
</script>
