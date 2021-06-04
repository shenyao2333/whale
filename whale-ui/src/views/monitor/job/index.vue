<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="jobName">
        <el-input
          v-model="queryParams.jobName"
          placeholder="请输入任务名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务类型" prop="jobType">
        <el-select v-model="queryParams.jobType" placeholder="请选择任务类型" clearable size="small">
          <el-option
            v-for="dict in jobTypeOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select v-model="queryParams.jobStatus" placeholder="请选择任务状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPerm="['job_add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleEdit"
          v-hasPerm="['job_edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-video-play"
          size="mini"
          @click="handleStarts"
          v-hasPerm="['job_start']"
        >启动全部任务
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-video-pause"
          size="mini"
          @click="handleStops"
          v-hasPerm="['job_stop']"
        >暂停全部任务
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-refresh"
          size="mini"
          @click="handleRefreshs"
          v-hasPerm="['job_refresh']"
        >重置全部任务
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          icon="el-icon-s-operation"
          size="mini"
          @click="handleJobLog"
          v-hasPerm="['jobLog_view']"
        >日志
        </el-button>
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery"/>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch"/>
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="jobList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="任务编号" align="center" prop="id" width="80"/>
      <el-table-column label="任务名称" align="center" prop="jobName" :show-overflow-tooltip="true"/>
      <el-table-column label="任务组名" align="center" prop="jobGroup"/>
      <el-table-column label="任务类型" align="center" prop="jobType" :formatter="jobTypeFormat"/>
      <el-table-column label="执行路径/文件.方法" align="center" prop="executePath" :show-overflow-tooltip="true"/>
      <el-table-column label="执行参数" align="center" prop="paramsValue" :show-overflow-tooltip="true"/>
      <el-table-column label="cron表达式" align="center" prop="cronExpression" :show-overflow-tooltip="true"/>
      <el-table-column label="上次执行时间" align="center" prop="previousTime" :show-overflow-tooltip="true"/>
      <el-table-column label="下次执行时间" align="center" prop="nextTime" :show-overflow-tooltip="true"/>
      <el-table-column label="任务状态" align="center" prop="jobStatus" :formatter="statusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-caret-right"
            @click="handleRun(scope.row)"
            v-hasPerm="['job_start']"
          >运行一次
          </el-button>
          <el-button
            v-if="scope.row.jobStatus != 2"
            size="mini"
            type="text"
            icon="el-icon-video-play"
            @click="handleStart(scope.row)"
            v-hasPerm="['job_start']"
          >启动
          </el-button>
          <el-button
            v-if="scope.row.jobStatus == 2"
            size="mini"
            type="text"
            icon="el-icon-video-pause"
            @click="handleStop(scope.row)"
            v-hasPerm="['job_stop']"
          >暂停
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPerm="['job_view']"
          >详细
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            v-hasPerm="['user_del']"
          >删除</el-button>
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

    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="720px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="jobName">
              <el-input v-model="form.jobName" placeholder="请输入任务名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务组名" prop="jobGroup">
              <el-input v-model="form.jobGroup" placeholder="请输入任务组名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务类型" prop="jobType">
              <el-select v-model="form.jobType" placeholder="请选择">
                <el-option
                  v-for="dict in jobTypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="错误策略" prop="misfirePolicy">
              <el-radio-group v-model="form.misfirePolicy" size="small">
                <el-radio-button label="1">立即执行</el-radio-button>
                <el-radio-button label="2">执行一次</el-radio-button>
                <el-radio-button label="3">放弃执行</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="executePath" label-width="180px">
              <span slot="label">
                执行路径/文件.方法
                <el-tooltip placement="top">
                  <div slot="content">
                    Jar包调用示例：/opt/entfrm-web.jar
                    <br/>Rest调用示例：http://47.100.3.58/
                    <br/>Bean调用示例：demo.demoMethod
                    <br/>Class类调用示例：com.entfrm.core.base.util.AddressUtil.getCityInfo
                    <br/>参数说明：支持字符串，布尔类型，长整型，浮点型，整型，指令等
                  </div>
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-input v-model="form.executePath" placeholder="请输入执行路径/文件.方法"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行参数" prop="paramsValue">
              <el-input v-model="form.paramsValue" placeholder="请输入执行参数"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="cron表达式" prop="cronExpression">
              <el-input v-model="form.cronExpression" placeholder="请输入cron执行表达式"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" type="textarea" rows="3" placeholder="请输入备注"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 任务日志详细 -->
    <el-dialog title="任务详细" :visible.sync="openView" width="720px">
      <el-form ref="form" :model="form" label-width="120px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务编号：">{{ form.id }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务组名：">{{ form.jobGroup }}</el-form-item>
            <el-form-item label="任务类型：">{{ jobTypeFormat(form) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行路径/文件.方法：" label-width="180px">{{ form.executePath }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="cron表达式：">{{ form.cronExpression }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次执行时间：">{{ parseTime(form.nextTime) }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行策略：">
              <div v-if="form.misfirePolicy == 0">默认策略</div>
              <div v-else-if="form.misfirePolicy == 1">立即执行</div>
              <div v-else-if="form.misfirePolicy == 2">执行一次</div>
              <div v-else-if="form.misfirePolicy == 3">放弃执行</div>
            </el-form-item>
            <el-form-item label="创建时间：">{{ form.createTime }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务状态：">{{ statusFormat(form) }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listJob,
    getJob,
    addJob,
    editJob,
    delJob,
    runJob,
    startJob,
    stopJob,
    startJobs,
    stopJobs,
    refreshJobs
  } from "@/api/monitor/job";

  export default {
    name: "Job",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 定时任务表格数据
        jobList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否显示详细弹出层
        openView: false,
        // 任务组名字典
        jobTypeOptions: [],
        // 状态字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          current: 1,
          size: 10,
          jobName: undefined,
          jobType: undefined,
          status: undefined
        },
        // 显示搜索条件
        showSearch: true,
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          jobName: [
            {required: true, message: "任务名称不能为空", trigger: "blur"}
          ],
          jobType: [
            {required: true, message: "任务类型不能为空", trigger: "blur"}
          ],
          jobGroup: [
            {required: true, message: "任务组名不能为空", trigger: "blur"}
          ],
          executePath: [
            {required: true, message: "执行路径/文件.方法不能为空", trigger: "blur"}
          ],
          cronExpression: [
            {required: true, message: "cron执行表达式不能为空", trigger: "blur"}
          ]
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("job_type").then(response => {
        this.jobTypeOptions = response.data;
      });
      this.getDicts("job_status").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 查询定时任务列表 */
      getList() {
        this.loading = true;
        listJob(this.queryParams).then(response => {
          this.jobList = response.data;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 任务类型名字典翻译
      jobTypeFormat(row, column) {
        return this.selectDictLabel(this.jobTypeOptions, row.jobType);
      },
      // 状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.jobStatus);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          jobName: undefined,
          jobType: undefined,
          invokeTarget: undefined,
          cronExpression: undefined,
          misfirePolicy: 1,
          concurrent: 1,
          status: "0"
        };
        this.resetForm("form");
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
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      /* 运行一次 */
      handleRun(row){
        this.$confirm('确认要运行一次"' + row.jobName + '"任务吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return runJob(row.jobName, row.jobGroup);
        }).then(() => {
          this.getList();
          this.msgSuccess("执行成功");
        }).catch(function (err) {
          console.log(err)
        });
      },
      /* 启动任务 */
      handleStart(row) {
        this.$confirm('确认要启动"' + row.jobName + '"任务吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return startJob(row.id);
        }).then(() => {
          this.getList();
          this.msgSuccess("执行成功");
        }).catch(function (err) {
          console.log(err)
        });
      },
      /* 暂停任务 */
      handleStop(row) {
        this.$confirm('确认要暂停"' + row.jobName + '"任务吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return stopJob(row.id);
        }).then(() => {
          this.getList();
          this.msgSuccess("执行成功");
        }).catch(function () {
        });
      },
      /** 任务详细信息 */
      handleView(row) {
        getJob(row.id).then(response => {
          this.form = response.data;
          this.openView = true;
        });
      },
      /** 任务日志列表查询 */
      handleJobLog() {
        this.$router.push("/jobLog");
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加任务";
      },
      /** 修改按钮操作 */
      handleEdit(row) {
        this.reset();
        const id = row.id || this.ids;
        getJob(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改任务";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              editJob(this.form).then(response => {
                if (response.code === 0) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addJob(this.form).then(response => {
                if (response.code === 0) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDel(row) {
        const ids = row.id;
        this.$confirm('是否确认删除定时任务编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delJob(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      handleStarts() {
        this.$confirm('确认要启动全部任务吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return startJobs();
        }).then(() => {
          this.getList();
          this.msgSuccess("执行成功");
        }).catch(function () {
        });
      },
      handleStops() {
        this.$confirm('确认要暂停全部任务吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return stopJobs();
        }).then(() => {
          this.getList();
          this.msgSuccess("执行成功");
        }).catch(function () {
        });
      },
      handleRefreshs() {
        this.$confirm('确认要重置全部任务吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return refreshJobs();
        }).then(() => {
          this.getList();
          this.msgSuccess("执行成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm("是否确认导出所有定时任务数据项?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportJob(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      }
    }
  };
</script>
