<template>
  <div class="app-container">
    <el-form :process="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入流程名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择流程分类" clearable size="small">
          <el-option
            v-for="dict in categoryOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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

    <el-table v-loading="loading" :data="processList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="流程id" align="center" prop="processonDefinitionId"/>
      <el-table-column label="流程名称" align="center" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column label="流程分类" align="center" prop="category" :formatter="categoryFormat"/>
      <el-table-column label="流程标签" align="center" prop="key" :show-overflow-tooltip="true"/>
      <el-table-column label="版本号" align="center" prop="version"/>
      <el-table-column label="状态" align="center" prop="suspend">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.suspend ? 'danger' : ''"
            effect="dark">
            {{scope.row.suspend ? '挂起' : '活动'}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="部署时间" align="center" prop="deploymentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deploymentTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-picture"
            @click="handleImg(scope.row)"
            v-hasPerm="['process_img']"
          >流程图
          </el-button>
          <el-button
            v-if="scope.row.suspend"
            size="mini"
            type="text"
            icon="el-icon-video-play"
            @click="handleChangeStatus(scope.row, 'active')"
            v-hasPerm="['process_change']"
          >激活
          </el-button>
          <el-button
            v-if="!scope.row.suspend"
            size="mini"
            type="text"
            icon="el-icon-video-pause"
            @click="handleChangeStatus(scope.row, 'suspend')"
            v-hasPerm="['process_change']"
          >挂起
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            v-hasPerm="['process_del']"
          >删除
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
    <el-dialog title="流程图" :visible.sync="showImgDialog" :fullscreen="true">
      <el-card class="box-card" style="overflow:auto;height: 100vh">
        <el-image :src="imgUrl"/>
      </el-card>
    </el-dialog>

  </div>
</template>

<script>
import {listProcess, changeStatus, delProcess} from "@/api/activiti/process";

export default {
  name: "Process",
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
      // 流程表格数据
      processList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示流程图
      showImgDialog: false,
      //流程图url
      imgUrl: '',
      // 流程分类数据字典
      categoryOptions: [],
      // 查询流程
      queryParams: {
        current: 1,
        size: 10,
        name: undefined,
        category: undefined,
        key: undefined
      },
      // 显示搜索条件
      showSearch: true,
      // 表单流程
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
    this.getDicts("model_category").then(response => {
      this.categoryOptions = response.data;
    });
  },
  methods: {
    /** 查询流程列表 */
    getList() {
      this.loading = true;
      listProcess(this.queryParams).then(response => {
          this.processList = response.data;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 流程分类字典翻译
    categoryFormat(row, column) {
      return this.selectDictLabel(this.categoryOptions, row.category);
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
      this.ids = selection.map(item => item.deploymentId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 流程图按钮操作 */
    handleImg(row) {
      this.imgUrl = process.env.VUE_APP_BASE_API + `/activiti/process/resource?procInsId=` + row.deploymentId + '&procDefId=' + row.processonDefinitionId + '&resType=image',
        this.showImgDialog = true
    },
    /** 改变状态按钮操作 */
    handleChangeStatus(row, status) {
      this.$confirm('确认要改变流程："' + row.name + '"的状态吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return changeStatus(row.processonDefinitionId, status);
      }).then(() => {
        this.getList();
        this.msgSuccess("操作成功");
      }).catch(function () {
      });
    },
    /** 删除按钮操作 */
    handleDel(row) {
      const ids = row.deploymentId;
      this.$confirm('确认要删除流程编号为"' + ids + '"的数据项吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delProcess(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    }
  }
};
</script>
