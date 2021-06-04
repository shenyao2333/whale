<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模型名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入模型名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择模型分类" clearable size="small">
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPerm="['model_add']"
        >新增
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

    <el-table v-loading="loading" :data="modelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="模型id" align="center" prop="id"/>
      <el-table-column label="模型名称" align="center" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column label="模型分类" align="center" prop="category" :formatter="categoryFormat"/>
      <el-table-column label="模型key" align="center" prop="key" :show-overflow-tooltip="true"/>
      <el-table-column label="版本号" align="center" prop="version"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后更新时间" align="center" prop="lastUpdateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastUpdateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-magic-stick"
            @click="handleEdit(scope.row)"
            v-hasPerm="['model_design']"
          >模型设计
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-success"
            @click="handleDeploy(scope.row)"
            v-hasPerm="['model_deploy']"
          >部署
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            v-hasPerm="['model_del']"
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

    <!-- 添加或修改模型配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="560px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模型名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入模型名称"/>
        </el-form-item>
        <el-form-item label="模型分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择模型分类">
            <el-option
              v-for="dict in categoryOptions"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模型key" prop="key">
          <el-input v-model="form.key" placeholder="请输入模型key"/>
        </el-form-item>
        <el-form-item label="模型描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入模型描述"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--工作流bpmn设计器-->
    <el-drawer
      direction="ttb"
      size="100%"
      title="工作流bpmn设计器"
      :visible.sync="drawer"
      :with-header="false"
      @close="drawerClose"
      destroy-on-close>
      <bpmn-designer :model-id="modelId"/>
    </el-drawer>

  </div>
</template>

<script>
import {listModel, getModel, delModel, addModel, deployModel} from "@/api/activiti/model";
import bpmnDesigner from "../detail"

export default {
  name: "Model",
  components: {
    bpmnDesigner
  },
  data() {
    return {
      //模型关联ID
      modelId: undefined,
      //抽屉显示条件
      drawer: false,
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
      // 模型表格数据
      modelList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 模型分类数据字典
      categoryOptions: [],
      // 查询模型
      queryParams: {
        current: 1,
        size: 10,
        name: undefined,
        category: undefined,
        key: undefined
      },
      // 显示搜索条件
      showSearch: true,
      // 表单模型
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "模型名称不能为空", trigger: "blur"}
        ],
        category: [
          {required: true, message: "模型分类不能为空", trigger: "blur"}
        ],
        key: [
          {required: true, message: "模型标签不能为空", trigger: "blur"}
        ],
        description: [
          {required: true, message: "模型描述不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("model_category").then(response => {
      this.categoryOptions = response.data;
    });
  },
  methods: {
    drawerClose() {
      this.getList()
    },
    /** 查询模型列表 */
    getList() {
      this.loading = true;
      listModel(this.queryParams).then(response => {
          this.modelList = response.data;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 模型分类字典翻译
    categoryFormat(row, column) {
      return this.selectDictLabel(this.categoryOptions, row.category);
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
        name: undefined,
        category: undefined,
        key: undefined,
        description: undefined
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加模型";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 模型按钮操作 */
    handleEdit(row) {
      this.modelId = row.id
      this.drawer = true
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addModel(this.form).then(response => {
            if (response.code === 0) {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            } else {
              this.msgError(response.msg);
            }
          });
        }
      });
    },
    /** 部署按钮操作 */
    handleDeploy(row) {
      this.$confirm('是否确认部署模型："' + row.name + '"?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return deployModel(row.id);
      }).then(() => {
        this.getList();
        this.msgSuccess("模型部署成功");
      }).catch(function () {
      });
    },
    /** 删除按钮操作 */
    handleDel(row) {
      const ids = row.id;
      this.$confirm('是否确认删除模型编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delModel(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    }
  }
};
</script>
