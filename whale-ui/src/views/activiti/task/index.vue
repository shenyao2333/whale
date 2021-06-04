<template>
  <div class="app-container">


    <el-tabs tab-position="left" style="height:80vh">
      <el-tab-pane label="我的任务">

        <el-form :task="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="任务名称" prop="taskName">
            <el-input
              v-model="queryParams.taskName"
              placeholder="请输入任务名称"
              clearable
              size="small"
              style="width: 240px"
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
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column label="任务id" align="center" prop="taskId"/>
          <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true"/>
          <el-table-column label="节点key" align="center" prop="nodeKey" :show-overflow-tooltip="true"/>
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.status == '1' ? 'danger' : ''"
                effect="dark">
                {{scope.row.status == '1' ? '待办' : '已办'}}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="提交时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-success"
                @click="handleCheck(scope.row)"
                v-hasPerm="['task_check']"
              >审批
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-tickets"
                @click="handleComment(scope.row, 'active')"
                v-hasPerm="['task_comment']"
              >审批意见
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-picture"
                @click="handleTrack(scope.row)"
                v-hasPerm="['task_track']"
              >流程追踪
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

      </el-tab-pane>
      <el-tab-pane label="历史任务">

        <el-form :task="historyQueryParams" ref="historyQueryForm" :inline="true" v-show="historyShowSearch" label-width="68px">
          <el-form-item label="任务名称" prop="taskName">
            <el-input
              v-model="historyQueryParams.taskName"
              placeholder="请输入任务名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="historyHandleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh-right" size="mini" @click="historyResetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">

          <div class="top-right-btn">
            <el-tooltip class="item" effect="dark" content="刷新" placement="top">
              <el-button size="mini" circle icon="el-icon-refresh" @click="historyHandleQuery"/>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" :content="historyShowSearch ? '隐藏搜索' : '显示搜索'" placement="top">
              <el-button size="mini" circle icon="el-icon-search" @click="historyShowSearch=!historyShowSearch"/>
            </el-tooltip>
          </div>
        </el-row>

        <el-table v-loading="historyLoading" :data="historyTaskList">
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column label="任务id" align="center" prop="taskId"/>
          <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true"/>
          <el-table-column label="节点key" align="center" prop="nodeKey" :show-overflow-tooltip="true"/>
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.status == '1' ? 'danger' : ''"
                effect="dark">
                {{scope.row.status == '1' ? '待办' : '已办'}}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="提交时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
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
          v-show="historyTotal>0"
          :total="historyTotal"
          :page.sync="historyQueryParams.current"
          :limit.sync="historyQueryParams.size"
          @pagination="getHistoryList"
        />

      </el-tab-pane>
    </el-tabs>


    <!-- 任务审批对话框 -->
    <el-dialog title="任务审批" :visible.sync="showCheckDialog" width="760px">
      <el-form :model="formData" label-width="100px" size="mini" :disabled="true">
        <component-integrated :form-code="formCode" :form-data="formData" :visible="showCheckDialog"/>
      </el-form>
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="24">
            <el-form-item label="审批意见：" >
              <el-input v-model="form.comment" type="textarea" :disabled="false" rows="3" placeholder="请输入审批意见"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          v-for="flag in flagList"
          :key="flag"
          type="primary"
          icon="el-icon-check"
          plain
          @click="handleTask(flag)">{{ flag }}
        </el-button>
        <el-button @click="showCheckDialog = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 审批意见对话框 -->
    <el-dialog title="审批意见" :visible.sync="showCommentDialog" width="760px">
      <el-table v-loading="loading" :data="commentList">
        <el-table-column label="任务id" align="center" prop="id"/>
        <el-table-column label="审批人" align="center" prop="userId" :show-overflow-tooltip="true"/>
        <el-table-column label="审批意见" align="center" prop="fullMessage" :show-overflow-tooltip="true"/>
        <el-table-column label="审批时间" align="center" prop="time" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.time) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 任务跟踪对话框 -->
    <el-dialog title="流程图" :visible.sync="showImgDialog" :fullscreen="true">
      <el-card class="box-card" style="overflow:auto;height: 100vh">
        <el-image :src="imgUrl"/>
      </el-card>
    </el-dialog>

    <!-- 流程图对话框 -->
    <el-dialog title="流程历史" :visible.sync="showHistoryImgDialog" :fullscreen="true" class="history">
      <el-tabs tab-position="left" style="height:82vh;overflow:auto;">
        <el-tab-pane label="流程追踪">
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
        </el-tab-pane>
        <el-tab-pane label="附加单据">

          <el-card class="box-card" style="overflow:auto;height: 82vh">
            <el-form :model="formData" label-width="100px" size="mini" :disabled="true">
              <component-integrated :form-code="formCode" :form-data="formData" :visible="showHistoryImgDialog"/>
            </el-form>
          </el-card>

        </el-tab-pane>
      </el-tabs>
    </el-dialog>

  </div>
</template>

<script>
import {listTask, getTask, checkTask, listHistoryTask, taskComment, getHistoryTask} from "@/api/activiti/task";
import {listDetailedProcess} from "@/api/activiti/iInitiatedProcess";
import componentIntegrated from '@/components/FormActivitiEngineDesign/FormIntegrated/ComponentIntegrated'
import {mergeFormData} from "@/utils/formActivitiEngineDesign/formIntegrated";
import request from '@/utils/request'

export default {
  name: "Task",
  components:{
    componentIntegrated
  },
  data() {
    return {
      // 模板编码
      formCode: "",
      // 遮罩层
      loading: true,
      historyLoading: true,
      detailedProcessLoading: true,
      // 总条数
      total: 0,
      historyTotal: 0,
      detailedProcessTotal: 0,
      // 任务表格数据
      taskList: [],
      historyTaskList: [],
      detailedProcessList: [],
      // 审批意见数据
      commentList: [],
      // 是否显示审批弹出层
      showCheckDialog: false,
      // 是否显示批注弹出层
      showCommentDialog: false,
      // 是否显示任务图
      showImgDialog: false,
      showHistoryImgDialog: false,
      flagList: {},
      //任务图url
      imgUrl: '',
      // 请假类型数据字典
      typeOptions: [],
      // 查询任务
      queryParams: {
        current: 1,
        size: 10,
        taskName: undefined
      },
      historyQueryParams: {
        current: 1,
        size: 10,
        taskName: undefined
      },
      detailedProcessQueryParams:{
        current: 1,
        size: 5
      },
      // 显示搜索条件
      showSearch: true,
      historyShowSearch: true,
      // 表单参数||合集
      form: {},
      //渲染表单
      formData: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
    this.getHistoryList();
    this.getDicts("leave_type").then(response => {
      this.typeOptions = response.data;
    });
  },
  methods: {
    /** 查询任务列表 */
    getList() {
      this.loading = true;
      listTask(this.queryParams).then(response => {
          this.taskList = response.data;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    getHistoryList() {
      this.historyLoading = true;
      listHistoryTask(this.historyQueryParams).then(response => {
          this.historyTaskList = response.data;
          this.historyTotal = response.total;
          this.historyLoading = false;
        }
      );
    },
    getListDetailedProcess() {
      this.detailedProcessLoading = true;
      listDetailedProcess(this.detailedProcessQueryParams).then(response => {
          this.detailedProcessList = response.data;
          this.detailedProcessTotal = response.total;
          this.detailedProcessLoading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 1;
      this.getList();
    },
    historyHandleQuery() {
      this.historyQueryParams.current = 1;
      this.getHistoryList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
    },
    historyResetQuery() {
      this.resetForm("historyQueryForm");
    },
    reset() {
      this.form = {}
      this.formData = {}
      this.flagList = {}
    },
    /** 流程审批按钮操作 */
    handleCheck(row) {
      this.reset();
      getTask(row.taskId).then(response => {
        let taskCheckData = response.data
        this.flagList = taskCheckData.flagList
        this.integratedForm(taskCheckData)
        this.showCheckDialog = true
      });
    },
    integratedForm(taskCheckData) {
      let businessKey=taskCheckData.businessKey,formCode=taskCheckData.formCode,formDataUrl=taskCheckData.formDataUrl;
      this.formCode=formCode
      if(this.formCode){
        mergeFormData(this.formCode, this.formData, this.rules).then(() => {
          //是否自定义数据集
          if(formDataUrl){
            request({
              url: formDataUrl,
              method: 'get',
              params: {businessKey:businessKey}
            }).then(response=>{
              this.form=response.data
              this.processResultMerge(response.data)
            })
          }else{
            this.form=taskCheckData
            this.processResultMerge(taskCheckData)
          }
        })
      }else{
        this.msgError("请先配置动态表单,目前没有动态表单可用!");
      }
    },
    /** 数据集合并处理方法 **/
    processResultMerge(data){
      if(data.type){
        data.type=this.selectDictLabel(this.typeOptions, data.type)
      }
      for (const key in data) {
        if(this.formData.hasOwnProperty(key)){
          this.$set(this.formData[key],'defaultValue', data[key])
        }else{
          this.$set(this.formData, key, data[key])
        }
      }
    },
    /** 处理历史 **/
    handleHistory(row) {
      this.imgUrl = process.env.VUE_APP_BASE_API + `/activiti/IInitiatedProcess/tracking?processInstanceId=`+row.processInstanceId
      this.detailedProcessQueryParams={...this.detailedProcessQueryParams,procInstId:row.processInstanceId}
      this.getListDetailedProcess()
      this.reset();
      getHistoryTask(row.taskId).then(response => {
        let taskCheckData = response.data
        this.flagList = taskCheckData.flagList
        this.integratedForm(taskCheckData)
        this.showHistoryImgDialog = true
      });
    },
    /** 流程批注按钮操作 */
    handleComment(row) {
      taskComment(row.taskId).then(response => {
        console.log(JSON.stringify(response.data))
        this.commentList = response.data;
        this.showCommentDialog = true
      });
    },
    handleTask: function (result) {
      this.form.taskFlag = result
      checkTask(this.form).then(response => {
        if (response.code === 0) {
          this.msgSuccess("操作成功");
          this.showCheckDialog = false;
          this.getList();
        } else {
          this.msgError(response.msg);
        }
      });
    },
    /** 流程追踪按钮操作 */
    handleTrack(row) {
      this.imgUrl = process.env.VUE_APP_BASE_API + `/activiti/task/track/` + row.taskId,
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
