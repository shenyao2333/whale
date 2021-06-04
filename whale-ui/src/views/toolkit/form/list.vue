<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleOnline"
          v-hasPerm="['form_add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPerm="['form_del']"
        >删除
        </el-button>
      </el-col>
      <!--      <el-col :span="1.5">
              <el-button
                type="success"
                icon="el-icon-edit"
                size="mini"
                :disabled="single"
                @click="handleUpdate"
                v-hasPerm="['form_edit']"
              >修改
              </el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                icon="el-icon-download"
                size="mini"
                @click="handleExport"
                v-hasPerm="['form_export']"
              >导出
              </el-button>
            </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="formList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column v-for="(column, index) in columnList" v-if="column.isList == '1'" :key="index"
                       :label="column.columnComment" align="center" :prop="column.javaField" width="180">
        <template slot-scope="scope">
          <span>{{scope.row[`${column.columnName}`] ===undefined ? '' : scope.row[`${column.columnName}`]}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-magic-stick"
            @click="handleDetail(scope.row)"
            v-hasPerm="['form_design']"
          >查看
          </el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPerm="['form_edit']"
          >修改
          </el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPerm="['form_del']"
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

    <!-- 添加或修改表单管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="表单名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入表单名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表单类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择表单类型">
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表单编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入表单编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本号" prop="version">
              <el-input v-model="form.version" placeholder="请输入版本号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库别名" prop="dsAlias">
              <el-input v-model="form.dsAlias" placeholder="请输入数据库别名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表名" prop="tableName">
              <el-input v-model="form.tableName" placeholder="请输入表名"/>
            </el-form-item>
          </el-col>

          <!--        <el-form-item label="自动建表">
                    <el-radio-group v-model="form.autoCreate">
                      <el-radio
                        v-for="dict in autoCreateOptions"
                        :key="dict.value"
                        :label="dict.value"
                      >{{ dict.label }}
                      </el-radio>
                    </el-radio-group>
                  </el-form-item>-->

        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="dtitle" :visible.sync="dopen" append-to-body>
      <avue-form :option="dynamicForm.option" v-model="dynamicForm.obj" ></avue-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  getForm,
  delForm,
  addForm,
  editForm,
  exportForm,
  listDynamicForm,
  getDynamicForm,
  removeDynamicForm,
  getColumns
} from "@/api/toolkit/form";

export default {
  name: "DynamicForm",
  components: {},
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
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表单管理表格数据
      formList: [],
      columnList: [],
      // 弹出层标题
      title: "",
      dtitle: "",
      // 是否显示弹出层
      open: false,
      dopen: false,
      // 是否显示弹出层
      openDesign: false,
      // 日期范围
      dateRange: [],
      statusOptions: [],
      // 表单类型字典
      typeOptions: [],
      // 是否自动建表字典
      autoCreateOptions: [],
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        name: null,
        type: null,
        tableName: null,
        createTime: null,
        params: {}
      },
      options: {
        column: [],
      },
      includeFields: ['input', 'password', 'textarea', 'number', 'radio', 'checkbox', 'select', 'upload', 'date', 'time', 'datetime'],
      // 表单参数
      form: {},
      dynamicForm:{
        option: {},
        obj: {}
      },
      // 表单校验
      rules: {
        name: [
          {required: true, message: "表单名称不能为空", trigger: "blur"}
        ],
        code: [
          {required: true, message: "表单编码不能为空", trigger: "blur"}
        ],
        version: [
          {required: true, message: "版本号不能为空", trigger: "blur"}
        ],
        dsAlias: [
          {required: true, message: "数据库别名不能为空", trigger: "blur"}
        ],
        tableName: [
          {required: true, message: "表名不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getColumnList();
    this.getList();
  },
  methods: {
    /** 查询表单管理列表 */
    getList() {
      this.loading = true;
      this.queryParams.tableName = this.$route.query.tableName
      listDynamicForm(this.queryParams).then(response => {
        this.formList = response.data;
        this.total = response.total;
        this.loading = false;
      });
    },
    getColumnList() {
      this.loading = true;
      const tableName = this.$route.query.tableName
      getColumns(tableName).then(response => {
        this.columnList = response.data;
        this.loading = false;
      });
    },
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 表单类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 是否自动建表字典翻译
    autoCreateFormat(row, column) {
      return this.selectDictLabel(this.autoCreateOptions, row.autoCreate);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        code: null,
        type: "0",
        dsAlias: null,
        tableName: null,
        data: null,
        autoCreate: "0",
        version: null
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
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleOnline() {
      const id = this.$route.query.id
      let routeUrl = this.$router.resolve({
        path: "/online",
        query: {id: id}
      });
      window.open(routeUrl.href, '_blank');
    },
    handleDetail(row){
      const tableName = this.$route.query.tableName
      getDynamicForm({tableName: tableName, id: row.id}).then(response => {
        this.dynamicForm.obj = response.data;
        this.dynamicForm.option = JSON.parse(response.msg)
        this.dynamicForm.option.detail = true
        this.dopen = true;
        this.dtitle = "详情";
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加表单管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getForm(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改表单管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            editForm(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addForm(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDesign(row) {

    },
    handleSubmit() {
      this.$refs.formDesign.getData('json').then(data => {
        this.form.data = JSON.stringify(data)
        editForm(this.form).then(response => {
          this.msgSuccess("表单设计数据提交成功");
          this.openDesign = false;
        });
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const tableName = this.$route.query.tableName
      this.$confirm('是否确认删除动态表单编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return removeDynamicForm({tableName: tableName, ids: ids});
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有表单管理数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportForm(queryParams);
      }).then(response => {
        this.download(response.data);
      })
    }
  }
};
</script>
<style scoped>
/deep/ .el-dialog__body {
  padding: 10px 20px;
}
</style>
