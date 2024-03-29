<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="部署ID" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入部署ID"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部署名称" prop="nameLike">
          <el-input
            v-model="queryParams.nameLike"
            placeholder="请输入部署名称"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPerm="['datasource_add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleEdit"
          v-hasPerm="['datasource_edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDel"
          v-hasPerm="['datasource_del']"
        >删除</el-button>
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="deployList"
              border @selection-change="handleSelectionChange">
      <el-table-column label="部署ID" align="center" prop="id" />
      <el-table-column label="流程名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="来源" align="center" prop="source" />
      <el-table-column label="创建时间" align="center" prop="deploymentTime" width="180">
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
            v-hasPerm="['datasource_edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            v-hasPerm="['datasource_del']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.firstResult"
      :limit.sync="queryParams.maxResults"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="70%" >
      <div slot="title" class="dialog-pane">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      <el-form ref="form" :model="form" :rules="rules" >
        <bpmn />
      </el-form>

    </el-dialog>
  </div>
</template>

<script>
import { listForm ,count } from "@/api/workflow/deploy";
import bpmn from "../index";


export default {
  components: { bpmn },
  name: "Datasource",
  data() {
    return {
      ids:'',
      multiple:'',
      single:undefined,
      // 遮罩层
      loading: true,
      dateRange:'',
      // 总条数
      total: 0,
      // 数据库表格数据multiple
      deployList: [],
      // 弹出层标题
      title: "流程图",
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      // 查询数据库
      queryParams: {
        firstResult: 0,
        maxResults: 10,
        id: undefined,
        nameLike: undefined,
        sortBy: 'deploymentTime',
        sortOrder: 'desc'
      },
      // 显示搜索条件
      showSearch: true,
      // 表单数据库
      form: {},
      // 表单校验
      rules: {

      }
    };
  },
  created() {
    this.getList()
    this.count();
  },
  methods: {
    /** 查询数据库列表 */
    getList() {
      this.loading = true;
      listForm(this.queryParams).then(response => {
          this.deployList = response;
          this.loading = false;
        }
      );
    },
    count(){
      count(this.queryParams).then(re =>{
        this.total=re.count;
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {

      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 0;
      this.getList();
      this.count();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加数据库";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      this.reset();
      const id = row.id || this.ids
      getDatasource(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改数据库";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("jlai ")
        }
      });
    },
    /** 删除按钮操作 */
    handleDel(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除数据库编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delDatasource(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function() {});
    }
  }
};
</script>
