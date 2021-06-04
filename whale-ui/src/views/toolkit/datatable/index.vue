<template>
  <div class="app-container">
    <el-row :gutter="10">
      <!--数据库名称数据-->
      <el-col :span="4" :xs="24">
        <el-card class="box-card">
          <div class="head-container">
            <el-input
              v-model="name"
              placeholder="请输入数据库名称"
              clearable
              size="small"
              prefix-icon="el-icon-search"
              style="margin-bottom: 20px"
            />
          </div>
          <div class="head-container">
            <el-tree
              :data="datasourceOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              ref="tree"
              default-expand-all
              @node-click="handleNodeClick"
            />
          </div>
        </el-card>
      </el-col>
      <!--数据表数据-->
      <el-col :span="20" :xs="24">
        <el-card class="box-card">
          <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="表名" prop="tableName">
              <el-input
                v-model="queryParams.tableName"
                placeholder="请输入表名"
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
                v-hasPerm="['datatable_add']"
              >新增表
              </el-button>
            </el-col>
            <!--<el-col :span="1.5">
              <el-button
                type="danger"
                icon="el-icon-delete"
                size="mini"
                :disabled="multiple"
                @click="handleDel"
                v-hasPerm="['datatable_del']"
              >删除表
              </el-button>
            </el-col>-->
            <el-col :span="1.5">
              <el-button
                type="success"
                icon="el-icon-download"
                size="mini"
                :disabled="multiple"
                @click="handleGen"
                v-hasPerm="['datatable_gen']"
              >生成
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

          <el-table v-loading="loading" :data="datatableList" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="54" align="center"/>
            <el-table-column label="表名" align="center" prop="tableName" :show-overflow-tooltip="true"/>
            <el-table-column label="注释" align="center" prop="tableComment" :show-overflow-tooltip="true"/>
            <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true"/>
            <el-table-column
              label="操作"
              align="center"
              width="240"
              class-name="small-padding fixed-width"
            >
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.isConfig === '1'"
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handlePreview(scope.row)"
                  v-hasPerm="['datatable_config']"
                >预览</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-setting"
                  @click="handleConfig(scope.row)"
                  v-hasPerm="['datatable_config']"
                >配置表
                </el-button>
                <el-button
                  v-if="scope.row.isConfig === '1'"
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDel(scope.row)"
                  v-hasPerm="['datatable_del']"
                >删除
                </el-button>
                <el-button
                  v-else
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDrop(scope.row)"
                  v-hasPerm="['datatable_drop']"
                >删除原表
                </el-button>
                <el-button
                  v-if="scope.row.isConfig === '1'"
                  size="mini"
                  type="text"
                  icon="el-icon-download"
                  @click="handleGen(scope.row)"
                  v-hasPerm="['datatable_gen']"
                >生成
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="720px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据表昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入数据表昵称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号码" maxlength="11"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据表名称" prop="datatableName">
              <el-input v-model="form.datatableName" placeholder="请输入数据表名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remarks" type="textarea" placeholder="请输入备注"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 预览界面 -->
    <el-dialog :title="preview.title" :visible.sync="preview.open" width="80%" top="5vh" append-to-body>
      <el-tabs v-model="preview.activeName">
        <el-tab-pane
          v-for="(value, key) in preview.data"
          :label="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :name="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :key="key"
        >
          <pre><code class="hljs" v-html="highlightedCode(value, key)"></code></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
  import {
    addDatatable,
    batchGenToLocal,
    previewTable,
    delDatatable,
    editDatatable,
    listDatatable,
    dropDataTable
  } from "@/api/toolkit/datatable";
  import {datasourceList} from "@/api/toolkit/datasource";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import {downLoadZip} from "@/utils/zipdownload";
  import hljs from "highlight.js/lib/highlight";
  import "highlight.js/styles/github-gist.css";
  hljs.registerLanguage("java", require("highlight.js/lib/languages/java"));
  hljs.registerLanguage("xml", require("highlight.js/lib/languages/xml"));
  hljs.registerLanguage("html", require("highlight.js/lib/languages/xml"));
  hljs.registerLanguage("vue", require("highlight.js/lib/languages/xml"));
  hljs.registerLanguage("javascript", require("highlight.js/lib/languages/javascript"));
  hljs.registerLanguage("sql", require("highlight.js/lib/languages/sql"));

  export default {
    name: "Datatable",
    components: {Treeselect},
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
        // 数据表表格数据
        datatableList: null,
        // 弹出层标题
        title: "",
        // 数据库名称树选项
        datasourceOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 数据库名称
        name: undefined,
        // 日期范围
        dateRange: [],
        // 状态数据字典
        statusOptions: [],
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "name"
        },
        // 预览参数
        preview: {
          open: false,
          title: "代码预览",
          data: {},
          activeName: "entity.java"
        },
        // 查询参数
        queryParams: {
          alias: undefined
        },
        // 显示搜索条件
        showSearch: true,
        // 表单校验
        rules: {
          datatableName: [
            {required: true, message: "数据表名称不能为空", trigger: "blur"}
          ]
        }
      };
    },
    watch: {
      // 根据名称筛选数据库名称树
      name(val) {
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.getDatasourceList();
      this.getDicts("status").then(response => {
        this.statusOptions = response.data;
      });
      this.loading = false;
    },
    methods: {
      /** 查询数据表列表 */
      getList() {
        this.loading = true;
        listDatatable(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.datatableList = response.data;
            this.loading = false;
          }
        );
      },
      /** 转换数据库数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.id,
          label: node.name,
          children: node.children
        };
      },
      /** 查询数据库名称结构 */
      getDatasourceList() {
        datasourceList().then(response => {
          this.datasourceOptions = response.data;
          if (this.datasourceOptions.length > 0) {
            //默认查询第一个数据源节点表配置
            this.queryParams.alias = this.datasourceOptions[0].alias;
            this.getList();
          }
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.alias = data.alias;
        this.getList();
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
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        if (!this.queryParams || this.queryParams.alias == undefined) {
          this.msgWarning("请选择数据库！");
          return;
        }
        this.queryParams.page = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.tableNames = selection.map(item => item.tableName);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      /** 新增按钮操作 */
      handleAdd() {
        //this.msgWarning("请选择数据库！");
        this.$router.push({path: "/gen/edit", query: {tableName: '', tableComment: ''}});
      },
      /** 修改按钮操作 */
      handleEdit(row) {
        this.reset();
        const id = row.id || this.ids;

      },
      /** 配置按钮操作 */
      handleConfig(row) {
        const tableName = row.tableName;
        const tableComment = row.tableComment;
        this.$router.push({path: "/gen/edit", query: {tableName: tableName, tableComment: tableComment}});
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let getNameById = (deptId, deptOptions) => {
              for (let i = 0; i < deptOptions.length; i++) {
                if (deptOptions[i].id === deptId) {
                  return deptOptions[i].name;
                }
                if (deptOptions[i].children) {
                  return getNameById(deptId, deptOptions[i].children);
                }
              }
              return "";
            };
            this.form.deptName = getNameById(this.form.deptId, this.deptOptions);
            if (this.form.id != undefined) {
              editDatatable(this.form).then(response => {
                if (response.code === 0) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addDatatable(this.form).then(response => {
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
        var that = this;
        if (!this.queryParams || this.queryParams.alias == undefined) {
          this.msgWarning("请选择数据库！");
          return;
        }
        const tableName = row.tableName;
        this.$confirm('是否确认删除数据表为"' + tableName + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDatatable(that.queryParams.alias, tableName);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function (e) {
          console.log(e);
        });
      },
      handleDrop(row) {
        var that = this;
        if (!this.queryParams || this.queryParams.alias == undefined) {
          this.msgWarning("请选择数据库！");
          return;
        }
        const tableName = row.tableName;
        this.$confirm('是否确认删除数据库表"' + tableName + '"?删除后无法恢复！', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "alert"
        }).then(function () {
          return dropDataTable(that.queryParams.alias, tableName);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function (e) {
          console.log(e);
        });
      },
      handlePreview(row){
        previewTable(row.tableName).then(response => {
          this.preview.data = response.data;
          this.preview.open = true;
        });
      },
      /** 高亮显示 */
      highlightedCode(code, key) {
        const vmName = key.substring(key.lastIndexOf("/") + 1, key.indexOf(".vm"));
        var language = vmName.substring(vmName.indexOf(".") + 1, vmName.length);
        const result = hljs.highlight(language, code || "", true);
        return result.value || '&nbsp;';
      },
      /** 生成按钮操作 */
      handleGen(row) {
        var tables = '';
        if (row.tableName) {
          tables = row.tableName
        } else {
          tables = this.tableNames.join()
        }

        if (row.genWay == '1') {
          batchGenToLocal(tables).then(response => {
            if (response.code === 0) {
              this.msgSuccess(response.data);
              this.getList();
            } else {
              this.msgError(response.msg);
            }
          });
        } else {
          downLoadZip("/toolkit/datatable/batchGenCode/" + tables, "entfrm");
        }
      }
    }
  };
</script>
