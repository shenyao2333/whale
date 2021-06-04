<template>
  <div class="app-container">
    <el-form :task="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程编号" prop="procDefId">
        <el-input
          v-model="queryParams.procDefId"
          placeholder="请输入流程编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程名称" prop="processName">
        <el-input
          v-model="queryParams.processName"
          placeholder="请输入流程名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh-right" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery"/>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch"/>
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="taskList">
      <el-table-column label="业务标题" align="center" prop="businessTitle" width="120"/>
      <el-table-column label="流程名称" align="center" prop="processName" width="120"/>
      <el-table-column label="流程编号" align="center" prop="procDefId" width="120"/>
      <el-table-column label="流程实例ID" align="center" prop="procInstId" width="120"/>
      <el-table-column label="发起人" align="center" prop="startUserId" width="120"/>
      <el-table-column label="开始日期" align="center" prop="startTime" width="120" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="120" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="耗时" align="center" prop="duration" width="120" :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="status" width="120" :formatter="statusFormat"/>
      <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == 1"
            size="mini"
            type="text"
            icon="el-icon-success"
            @click="abolishProcess(scope.row)"
          >作废流程
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-picture"
            @click="handleHistory(scope.row)"
          >历史
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.current"
      :limit.sync="queryParams.size"
      @pagination="getList"
    />

    <!-- 流程图对话框 -->
    <el-dialog title="流程历史" :visible.sync="showImgDialog" :fullscreen="true" class="history">
      <el-row type="flex" justify="center" align="middle">
        <div style="width: 230px">
          图例 : &nbsp;
          <span style="color:#017501">
            <svg-icon icon-class="square" class="prompt"/> 已完成
          </span>
          &nbsp;
          <span style="color:#e90606">
            <svg-icon icon-class="square" class="prompt"/> 进行中
          </span>
          &nbsp;
          <span style="color:#585858">
             <svg-icon icon-class="square" class="prompt"/> 未执行
          </span>
        </div>
      </el-row>
      <el-divider/>
      <div style="overflow:auto;height: 431px" >
        <el-image :src="imgUrl"/>
      </div>
      <el-card class="box-card" style="overflow:auto;">
        <template #header>
          <div class="card-header">
            <span>流程历史跟踪</span>
          </div>
        </template>
        <el-table v-loading="detailedProcessLoading" :data="detailedProcessList">
          <el-table-column label="名称" align="center" prop="actName"/>
          <el-table-column label="流程实例ID" align="center" prop="procInstId"/>
          <el-table-column label="开始时间" align="center" prop="startTime"/>
          <el-table-column label="结束时间" align="center" prop="endTime"/>
          <el-table-column label="负责人" align="center" prop="assignee"/>
          <el-table-column label="处理结果" align="center" prop="processResult"/>
          <el-table-column label="处理意见" align="center" prop="message"
                           :show-overflow-tooltip="true"/>
        </el-table>
        <pagination
          v-show="detailedProcessTotal>0"
          :total="detailedProcessTotal"
          :page.sync="detailedProcessQueryParams.current"
          :limit.sync="detailedProcessQueryParams.size"
          @pagination="getListDetailedProcess"
        />

      </el-card>
    </el-dialog>

  </div>
</template>

<script>
import {listIInitiatedProcess, abolishProcess, listDetailedProcess } from "@/api/activiti/iInitiatedProcess";
import svgIcon from "@/components/SvgIcon"

export default {
  name: "iInitiatedProcess",
  components:{
    svgIcon
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 遮罩层
      detailedProcessLoading: true,
      // 总条数
      total: 0,
      // 查询审批流程的流程详细总条数
      detailedProcessTotal: 0,
      // 是否显示流程图
      showImgDialog: false,
      //流程图url
      imgUrl: '',
      // 任务表格数据
      taskList: [],
      // 审批流程的流程详细表格数据
      detailedProcessList: [],
      // 弹出层标题
      title: "",
      // 状态字典
      statusOptions: [],
      // 请假类型数据字典
      typeOptions: [],
      // 查询任务
      queryParams: {
        current: 1,
        size: 10,
        procDefId: undefined,
        processName: undefined
      },
      // 查询审批流程的流程详细
      detailedProcessQueryParams:{
        current: 1,
        size: 5
      },
      // 显示搜索条件
      showSearch: true
    };
  },
  created() {
    this.getList();
    this.getDicts("leave_status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    /** 查询审批流程的流程列表 */
    getListDetailedProcess() {
      this.detailedProcessLoading = true;
      listDetailedProcess(this.detailedProcessQueryParams).then(response => {
          this.detailedProcessList = response.data;
          this.detailedProcessTotal = response.total;
          this.detailedProcessLoading = false;
        }
      );
    },
    /** 查询任务列表 */
    getList() {
      this.loading = true;
      listIInitiatedProcess(this.queryParams).then(response => {
          this.taskList = response.data;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
    },
    abolishProcess(row) {
      this.$confirm('是否作废流程ID为' + row.procInstId + '的数据项?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return abolishProcess(
          {procInstId: row.procInstId, businessKey: row.businessKey})
      }).then(() => {
        this.getList();
        this.msgSuccess("作废成功");
      })
    },
    handleHistory(row) {
      this.imgUrl = process.env.VUE_APP_BASE_API + `/activiti/IInitiatedProcess/tracking?processInstanceId=`+row.procInstId
      this.detailedProcessQueryParams={...this.detailedProcessQueryParams,procInstId:row.procInstId}
      this.getListDetailedProcess()
      this.showImgDialog = true
    }
  }
};
</script>

<style lang='scss'>

.history {

  /*滚动条 start*/
  ::-webkit-scrollbar {
    width: 6px;
    height: 4px;
    background-color: #F5F5F5;
    visibility: visible;
  }

  /*定义滚动条轨道 内阴影+圆角*/
  ::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 6px #ffffff;
    background: #ffffff;
  }

  /*定义滑块 内阴影+圆角*/
  ::-webkit-scrollbar-thumb {
    border-radius: 3px;
    -webkit-box-shadow: inset 0 0 6px rgb(211, 212, 211);
    background-color: rgb(222, 224, 223);
  }

}

.prompt{
  width: 20px;
  height: 20px;
}

.card-header {
  font-size: 18px;
}



</style>
