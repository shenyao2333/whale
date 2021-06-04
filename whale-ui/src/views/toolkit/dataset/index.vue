<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="格式类型" prop="ftype">
        <el-select v-model="queryParams.ftype" placeholder="请选择格式类型" clearable size="small">
          <el-option
            v-for="dict in ftypeOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange"
                        size="small"
                        style="width: 240px"
                        value-format="yyyy-MM-dd"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPerm="['dataset_add']"
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
          v-hasPerm="['dataset_edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDel"
          v-hasPerm="['dataset_del']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPerm="['dataset_export']"
        >导出
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

    <el-table v-loading="loading" :data="datasetList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="编号" align="center" prop="id"/>
      <el-table-column label="名称" align="center" prop="name"/>
      <el-table-column label="格式类型" align="center" prop="ftype" :formatter="ftypeFormat"/>
      <el-table-column label="sql脚本" align="center" prop="scripts" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
            v-hasPerm="['dataset_edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-data"
            @click="handleApi(scope.row)"
            v-hasPerm="['dataset_edit']"
          >数据接口
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            v-hasPerm="['dataset_del']"
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

    <!-- 添加或修改数据源对话框 -->
    <el-dialog :title="title" :visible.sync="open">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库" prop="alias">
              <treeselect v-model="form.alias" :options="datasourceOptions" :normalizer="normalizer"
                          placeholder="请选择数据库"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="格式类型">
              <el-select v-model="form.ftype" placeholder="请选择格式类型">
                <el-option
                  v-for="dict in ftypeOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="sql脚本" prop="scripts">
              <codemirror class="custom" v-model="form.scripts" :options="cmOptions"
                          placeholder="请输入sql脚本"></codemirror>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作">
              <el-button type="primary" @click="analysisScripts">解析</el-button>
              <el-button type="primary" @click="handlePreview">预览数据</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否分页">
              <el-radio-group v-model="form.isPage">
                <el-radio
                  v-for="dict in pageOptions"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="解析结果">
              <el-table
                :data="analysisData"
                border
                style="width: 100%">
                <el-table-column
                  prop="columnName"
                  label="字段"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="dataType"
                  label="类型"
                  width="180">
                </el-table-column>
                <el-table-column label="标签" min-width="10%">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.javaField" placeholder="标签"></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="显示" min-width="5%">
                  <template slot-scope="scope">
                    <el-checkbox true-label="1" false-label="0" v-model="scope.row.isShow"></el-checkbox>
                  </template>
                </el-table-column>
                <el-table-column label="参数" min-width="5%">
                  <template slot-scope="scope">
                    <el-checkbox true-label="1" false-label="0" v-model="scope.row.isParam"></el-checkbox>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 预览数据 -->
    <el-drawer
      ref="drawer"
      title="预览数据"
      :visible.sync="dataDialog"
      direction="rtl"
      :show-close="false">

      <codemirror v-model="previewDatas" :options="cmOptions"></codemirror>

    </el-drawer>
  </div>
</template>

<script>
  import {
    listDataset,
    getDataset,
    delDataset,
    addDataset,
    editDataset,
    exportDataset,
    analysisScripts,
    previewData
  } from "@/api/toolkit/dataset";
  import {datasourceList} from "@/api/toolkit/datasource";
  import {codemirror} from 'vue-codemirror'
  import "codemirror/lib/codemirror.css";
  import "codemirror/theme/ambiance.css";  // 这里引入的是主题样式，根据设置的theme的主题引入，一定要引入！！
  require("codemirror/mode/sql/sql"); // 这里引入的模式的js，根据设置的mode引入，一定要引入！！
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "Dataset",
    components: {
      codemirror,
      Treeselect
    },
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
        // 数据源表格数据
        datasetList: [],
        // 解析结果数据
        analysisData: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        dataDialog: false,
        // 日期范围
        dateRange: [],
        // 格式类型字典
        ftypeOptions: [],      // 查询参数
        pageOptions: [],
        datasourceOptions: [],
        queryParams: {
          current: 1,
          size: 10,
          name: undefined,
          ftype: undefined,
          createTime: undefined,
        },
        // 显示搜索条件
        showSearch: true,
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            {required: true, message: "数据源名称不能为空", trigger: "blur"}
          ],
          alias: [
            {required: true, message: "数据库不能为空", trigger: "blur"}
          ],
          scripts: [
            {required: true, message: "sql脚本不能为空", trigger: "blur"}
          ],
        },
        previewDatas: undefined,
        cmOptions: {
          value: '',
          mode: "text/javascript",
          theme: "ambiance",
          readOnly: false,
          lineNumbers: true,
          line: true,
        }
      };
    },
    created() {
      this.getList();
      this.getDatasourceList();
      this.getDicts("data_type").then(response => {
        this.ftypeOptions = response.data;
      });
      this.getDicts("yes_no").then(response => {
        this.pageOptions = response.data;
      });
    },
    methods: {
      /** 查询数据源列表 */
      getList() {
        this.loading = true;
        listDataset(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.datasetList = response.data;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 格式类型字典翻译
      ftypeFormat(row, column) {
        return this.selectDictLabel(this.ftypeOptions, row.ftype);
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
          fType: 'json',
          cdata: undefined,
          scripts: undefined,
          alias: undefined,
          isPage: '1'
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
        this.ids = selection.map(item => item.id)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.alias,
          label: node.name,
          children: node.children
        };
      },
      /** 查询数据库 */
      getDatasourceList() {
        datasourceList().then(response => {
          this.datasourceOptions = response.data;
        });
      },
      /** 解析脚本 */
      analysisScripts() {
        if (!this.form.alias) {
          this.msgError("请选择数据源");
          return
        }
        if (!this.form.scripts) {
          this.msgError("请输入sql脚本");
          return
        }
        analysisScripts(this.form.alias, this.form.scripts).then(response => {
          this.analysisData = response.data;
        });
      },
      handlePreview() {
        if (!this.form.alias) {
          this.msgError("请选择数据源");
          return
        }
        if (!this.form.scripts) {
          this.msgError("请输入sql脚本");
          return
        }
        previewData(this.form.alias, this.form.scripts).then(response => {
          this.previewDatas = JSON.stringify(response.data);
          this.dataDialog = true
        });
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加数据源";
      },
      /** 修改按钮操作 */
      handleEdit(row) {
        this.reset();
        const id = row.id || this.ids
        getDataset(id).then(response => {
          this.form = response.data;
          this.analysisData = JSON.parse(this.form.cdata)
          this.open = true;
          this.title = "修改数据源";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.cdata = JSON.stringify(this.analysisData)
            if (this.form.id != undefined) {
              editDataset(this.form).then(response => {
                if (response.code === 0) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addDataset(this.form).then(response => {
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
      /** 数据接口 */
      handleApi(row) {
        this.$alert('http://' + location.host + process.env.VUE_APP_BASE_API + "/toolkit/dataset/api/" + row.ftype + "/" + row.code, "数据接口", { dangerouslyUseHTMLString: true });
      },
      /** 删除按钮操作 */
      handleDel(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除数据源编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDataset(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有数据源数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportDataset(queryParams);
        }).then(response => {
          this.download(response.data);
        }).catch(function () {
        });
      }
    }
  };
</script>
<style lang="scss" scoped>
  .custom > > > .CodeMirror {
    height: auto;
    min-height: 200px;
  }

  .custom > > > .CodeMirror-scroll {
    min-height: 200px;
  }

  .custom > > > .cm-s-rubyblue span.cm-string {
    color: #F08047;
  }

</style>
