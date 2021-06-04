<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--应用数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="name"
            placeholder="请输入应用名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="applicationOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--接口数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="接口名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入接口名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in statusOptions"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
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
            <el-button icon="el-icon-refresh-right" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-circle-plus-outline"
              size="mini"
              @click="handleQuickAdd"
              v-hasPerm="['apiinfo_add']"
            >快速新增
            </el-button>
          </el-col>
          <!--<el-col :span="1.5">
            <el-button
              type="success"
              icon="el-icon-success"
              size="mini"
              @click="handlePublish"
              v-hasPerm="['application_edit']"
            >更新所有接口
            </el-button>
          </el-col>-->

          <div class="top-right-btn">
            <el-tooltip class="item" effect="dark" content="刷新" placement="top">
              <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery"/>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
              <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch"/>
            </el-tooltip>
          </div>
        </el-row>

        <el-table
          v-loading="loading"
          :data="apiinfoList"
          row-key="id"
          border
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="name" label="接口名称" :show-overflow-tooltip="true" width="160"></el-table-column>
          <el-table-column prop="requestMethod" label="请求方法" width="120"></el-table-column>
          <el-table-column prop="requestType" label="请求方式" width="120"></el-table-column>
          <el-table-column prop="scripts" label="脚本" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="status" label="状态" :formatter="statusFormat" width="80"></el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime">
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
                v-hasPerm="['apiinfo_edit']"
              >修改
              </el-button>
              <el-button
                v-if="scope.row.parentId === 0"
                size="mini"
                type="text"
                icon="el-icon-plus"
                @click="handleAdd(scope.row)"
                v-hasPerm="['apiinfo_add']"
              >新增
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDel(scope.row)"
                v-hasPerm="['apiinfo_del']"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

      </el-col>
    </el-row>

    <!-- 快速添加api对话框 -->
    <el-dialog :title="quickTitle" :visible.sync="quickOpen" width="720px">
      <el-form ref="quickForm" :model="quickForm" :rules="quickRules" label-width="85px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="资源名称" prop="name">
              <el-input v-model="quickForm.name" placeholder="请输入资源名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资源Code" prop="code">
              <el-input v-model="quickForm.code" placeholder="请输入资源Code"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属应用" prop="applicationId">
              <treeselect v-model="quickForm.applicationId" :options="applicationOptions" :normalizer="normalizer2"
                          placeholder="请选择所属应用"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表名" prop="tableId">
              <treeselect v-model="quickForm.tableId" :options="datatableOptions" :normalizer="normalizer"
                          placeholder="请选择表名"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="类型">
              <el-checkbox-group v-model="quickForm.type">
                <el-checkbox label="新增"/>
                <el-checkbox label="修改"/>
                <el-checkbox label="删除"/>
                <el-checkbox label="列表"/>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="quickForm.remarks" type="textarea" placeholder="请输入备注"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitQuickForm">确 定</el-button>
        <el-button @click="quickOpen=false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="720px" @closed="handleClosed">
      <el-form ref="form" :model="form" :rules="rules" label-width="85px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="接口名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入接口名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属应用" prop="applicationId">
              <treeselect v-model="form.applicationId" :options="applicationOptions" :normalizer="normalizer2"
                          placeholder="请选择所属应用"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.type !== 'other'">
            <el-form-item label="表名" prop="tableId">
              <treeselect v-model="form.tableId" :options="datatableOptions" :normalizer="normalizer"
                          placeholder="请选择表名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.type !== 'other'">
            <el-form-item label="请求方式" prop="requestType">
              <el-select v-model="form.requestType" placeholder="请输入请求方式">
                <el-option label="Post请求" value="Post"></el-option>
                <el-option label="Get请求" value="Get"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.type !== 'other'">
            <el-form-item label="接口类型" prop="type">
              <el-select @change="interfaceTypeChange" v-model="form.type" placeholder="请选择接口类型" style="width: 100%">
                <el-option label="新增" value="insert"/>
                <el-option label="修改" value="update"/>
                <el-option label="删除" value="delete"/>
                <el-option label="列表" value="select"/>
                <el-option label="自定义" value="custom"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.type !== 'other'">
            <el-form-item label="请求方法" prop="requestMethod">
              <el-input v-model="form.requestMethod" placeholder="请输入请求方法"/>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="selectColumns && form.type != 'custom'" class="row1">
            <el-form-item label="操作参数">
              <div class="row1">
                <div class="item" v-for="it in selectColumns" :key="it.columnName">
                  <el-checkbox @change="handleChange(it, selectColumns)" v-model="it.checked">{{ it.name }}
                  </el-checkbox>
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="searchConditionColumns && form.type === 'select'" class="row2">
            <el-form-item label="查询参数">
              <div class="row2">
                <div class="item" v-for="it in searchConditionColumns" :key="it.columnName">
                  <el-checkbox @change="handleBottomChange(it, selectColumns)" v-model="it.checked">{{ it.name }}
                  </el-checkbox>
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.type !== 'other'">
            <el-form-item label="脚本" prop="scripts">
              <el-input v-model="form.scripts" :disabled="sqlDisabled" type="textarea" rows="5"
                        placeholder="请输入脚本"></el-input>
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

  </div>
</template>

<script>
  import {
    listApiinfo,
    getApiinfo,
    delApiinfo,
    addApiinfo,
    addApiinfos,
    editApiinfo,
    publishApiinfo,
    exportApiinfo
  } from "@/api/toolkit/apiinfo";
  import {treeApplication} from "@/api/system/application";
  import {treeDatatable, allColumnInfo} from "@/api/toolkit/datatable";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "Apiinfo",
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
        // 总条数
        total: 0,
        // 接口表格数据
        apiinfoList: [],
        // 弹出层标题
        title: "",
        // 应用名称树选项
        applicationOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 应用名称
        name: undefined,
        // 日期范围
        dateRange: [],
        // 状态数据字典
        statusOptions: [],
        // 表名字典
        datatableOptions: [],
        // 角色选项
        roleOptions: [],
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "name"
        },
        // 查询参数
        queryParams: {
          current: 1,
          size: 10,
          mame: undefined,
          status: undefined,
          applicationId: undefined
        },
        // 显示搜索条件
        showSearch: true,
        // 表单校验
        rules: {
          name: [
            {required: true, message: "接口名称不能为空", trigger: "blur"}
          ],
          applicationId: [
            {required: true, message: "所属应用不能为空", trigger: "blur"}
          ],
          tableId: [
            {required: true, message: "表名不能为空", trigger: "blur"}
          ],
          requestType: [
            {required: true, message: "请求方式不能为空", trigger: "blur"}
          ],
          type: [
            {required: true, message: "接口类型不能为空", trigger: "blur"}
          ],
          requestMethod: [
            {required: true, message: "请求方法不能为空", trigger: "blur"}
          ]
        },
        // 从服务器获取到的字段信息
        columnsInfo: [],
        isCustomInterface: false,
        // 上边一行的数据
        selectColumns: null,
        // 勾选的数据（查询的时候作为查询条件 下边一行的数据）
        searchConditionColumns: null,
        // 选择的表名
        tableName: undefined,
        // 编辑的时候会对表单进行重新赋值 会触发watch 禁止watch回调执行一次
        isStop: false,
        // 快速添加接口相关
        quickOpen: false,
        quickTitle: '',
        quickForm: {},
        quickRules: {
          name: [
            {required: true, message: "资源名称不能为空", trigger: "blur"}
          ],
          code: [
            {required: true, message: "资源Code不能为空", trigger: "blur"}
          ],
          applicationId: [
            {required: true, message: "所属应用不能为空", trigger: "blur"}
          ],
          tableId: [
            {required: true, message: "表名不能为空", trigger: "blur"}
          ]
        }
      };
    },
    computed: {
      sqlDisabled() {
        return !this.form.type || (this.form.type && this.form.type !== 'custom')
      }
    },
    watch: {
      // 根据名称筛选应用名称树
      name(val) {
        this.$refs.tree.filter(val);
      },
      'quickForm.tableId'(val) {
        if (val !== undefined && val !== null) {
          const target = this.find(this.datatableOptions, val, 'id')
          if (target) {
            this.tableName = target.tableName
          }
        }
      },
      // 选择了数据表，那么要重新获取字段信息
      'form.tableId'(val) {
        this.tableName = undefined
        if (val !== undefined && val !== null) {
          const target = this.find(this.datatableOptions, val, 'id')
          if (target) {
            this.tableName = target.tableName
          }
        }
        allColumnInfo(val)
          .then(res => {
            this.columnsInfo = res.data
            if (this.form.type) {
              this.getShowData(this.columnsInfo, this.form.type)
            }
          })
          .catch(err => {
          })
      }
    },
    created() {
      this.getList();
      this.getApplicationTree();
      this.getDatatableTree();
      this.getDicts("status").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 查找 */
      find(arr, val, key) {
        for (let i = 0; i < arr.length; i++) {
          if (arr[i][key] === val) {
            return arr[i]
          }
        }
        return null
      },
      /** 新增窗口关闭后需要清除一些数据 */
      handleClosed() {
        this.columnsInfo = []
        this.isCustomInterface = false
        this.selectColumns = null
        this.searchConditionColumns = null
        this.isStop = false
      },
      /** 根据type从data中找到要显示的数据 */
      getShowData(data, type) {
        if (this.isStop) {
          this.isStop = false
          return
        }
        let arr = []
        let filter = undefined
        for (let i = 0; i < this.columnsInfo.length; i++) {
          let checked = (type === 'delete' && this.columnsInfo[i].columnName === 'id') ||
            (type === 'insert' && this.columnsInfo[i].isAdd === '1') ||
            (type === 'update' && this.columnsInfo[i].isEdit === '1')
          arr.push({
            name: this.columnsInfo[i].columnComment,
            checked,
            columnName: this.columnsInfo[i].columnName
          })
        }
        this.selectColumns = arr
        if (type === 'select') {
          this.searchConditionColumns = JSON.parse(JSON.stringify(arr))
        } else {
          this.searchConditionColumns = []
        }

        this.autoMethodAndScripts(this.selectColumns, this.searchConditionColumns, type)
      },
      /** 接口类型变了就要重新显示可以被勾选的数据 */
      interfaceTypeChange(val) {
        this.isCustomInterface = val === 'custom'
        if (this.columnsInfo.length > 0) {
          this.getShowData(this.columnsInfo, val)
        }
      },
      /** 根据接口类型自动生成方法名和脚本 */
      autoMethodAndScripts(columns, columns1, type) {
        switch (type) {
          case 'insert':
            this.form.requestMethod = '/save'
            let sbf = 'insert into ' + this.tableName + ' ('
            let sbp = ''
            for (let i in columns) {
              if (columns[i].checked) {
                sbf += columns[i].columnName + ","
                sbp += "?,"
              }
            }
            sbf = sbf.substring(0, sbf.length - 1) + ') values ('
            sbp = sbp.substring(0, sbp.length - 1) + ')'
            this.form.scripts = sbf + sbp
            break
          case 'update':
            this.form.requestMethod = '/update'
            let sbu = "update " + this.tableName + ' set '
            for (let i in columns) {
              if (columns[i].checked) {
                sbu += columns[i].columnName + ' = ? ,'
              }
            }
            sbu = sbu.substring(0, sbu.length - 1)
            this.form.scripts = sbu
            break
          case 'delete':
            this.form.requestMethod = '/remove'
            let sbe = 'delete ' + this.tableName + ' where id = ?'
            this.form.scripts = sbe
            break
          case 'select':
            this.form.requestMethod = '/list'
            let sbs = 'select ';
            let sbw = '';
            for (let i in columns) {
              if (columns[i].checked) {
                sbs += columns[i].columnName + ' ' + this.toHump(columns[i].columnName) + ', '
              }
            }
            for (let m in columns1) {
              if (columns1[m].checked) {
                sbw += columns1[m].columnName + ' = ? and '
              }
            }
            sbs = sbs.substring(0, sbs.length - 2) + ' from ' + this.tableName + ' where '
            sbw = sbw.substring(0, sbw.length - 4)
            this.form.scripts = sbs + sbw
            break
          default:
            this.form.requestMethod = '/custom'
            this.form.scripts = ''
            break
        }
      },
      /** 下划线转换驼峰 */
      toHump(name) {
        return name.replace(/\_(\w)/g, function (all, letter) {
          return letter.toUpperCase();
        });
      },
      /** 驼峰转换下划线 */
      toUnderlineCase(name) {
        return name.replace(/([A-Z])/g, "_$1").toLowerCase();
      },
      /** 勾选/取消勾选调用这个函数 */
      handleChange(it, all) {
        this.autoMethodAndScripts(this.selectColumns, this.searchConditionColumns, this.form.type)
      },
      /** 勾选/取消勾选下边一行调用这个函数 */
      handleBottomChange(it, all) {
        this.autoMethodAndScripts(this.selectColumns, this.searchConditionColumns, this.form.type)
      },
      /** 查询接口列表 */
      getList() {
        this.loading = true;
        listApiinfo(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.apiinfoList = this.handleTree(response.data, "id");
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 转换应用数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.id,
          label: node.tableComment,
          children: node.children
        };
      },
      /** 转换应用数据结构2 */
      normalizer2(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.id,
          label: node.name,
          children: node.children
        };
      },
      /** 查询应用名称下拉树结构 */
      getApplicationTree() {
        treeApplication().then(response => {
          this.applicationOptions = response.data;
        });
      },
      getDatatableTree() {
        treeDatatable().then(response => {
          this.datatableOptions = response.data;
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.applicationId = data.id;
        this.getList();
      },
      // 菜单显示状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
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
          nickName: undefined,
          password: undefined,
          phone: undefined,
          email: undefined,
          sex: undefined,
          status: "0",
          remarks: undefined,
          roles: []
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
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
        this.ids = selection.map(item => item.id);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      quickReset() {
        this.quickForm = {
          name: '',
          applicationId: undefined,
          tableId: undefined,
          type: [],
          remarks: ''
        }
      },
      /** 快速新增按钮操作 */
      handleQuickAdd() {
        this.quickReset()
        this.quickOpen = true
        this.quickTitle = '快速添加接口'
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset();
        this.form.parentId = row.id
        this.getDatatableTree();
        this.open = true;
        this.title = "添加接口";
      },
      /** 修改按钮操作 */
      handleEdit(row) {
        this.reset();
        this.form.parentId = row.id
        this.isStop = true
        this.getDatatableTree();
        const id = row.id || this.ids;
        getApiinfo(id)
          .then(response => {
            this.form = response.data
            if (this.form.fdata) {
              this.selectColumns = JSON.parse(this.form.fdata)
            }
            if (this.form.pdata) {
              this.searchConditionColumns = JSON.parse(this.form.pdata)
            }
            this.open = true
            this.title = "修改接口"
            this.form.password = ""
          });
      },
      /** 重置密码按钮操作 */
      handleResetPwd(row) {
        this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(({value}) => {
          resetPwd(row.id, value).then(response => {
            if (response.code === 0) {
              this.msgSuccess("修改成功，新密码是：" + value);
            } else {
              this.msgError(response.msg);
            }
          });
        }).catch(() => {
        });
      },
      /** 快速提交按钮 */
      submitQuickForm: function () {
        this.$refs["quickForm"].validate(valid => {
          this.quickForm.type = this.quickForm.type.join()
          this.quickForm.tableName = this.tableName
          //console.log(JSON.stringify(this.quickForm))
          if (valid) {
            addApiinfos(this.quickForm).then(response => {
              if (response.code === 0) {
                this.msgSuccess("新增成功");
                this.quickOpen = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.selectColumns) {
              this.form.fdata = JSON.stringify(this.selectColumns)
            }
            if (this.form.type === 'select') {
              this.form.pdata = JSON.stringify(this.searchConditionColumns)
            }
            this.quickForm.tableName = this.tableName
            if (this.form.id != undefined) {
              editApiinfo(this.form).then(response => {
                if (response.code === 0) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addApiinfo(this.form).then(response => {
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
      handlePublish() {
        const applicationId = this.queryParams.applicationId;
        if (!applicationId) {
          this.msgWarning('请先选择应用')
          return;
        }
        this.$confirm('是否确认要更新所有接口吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return publishApiinfo(applicationId);
        }).then(() => {
          this.getList();
          this.msgSuccess("更新成功");
        }).catch(function () {
        });
      },
      /** 删除按钮操作 */
      handleDel(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除接口编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delApiinfo(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有接口数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportApiinfo(queryParams);
        }).then(response => {
          this.download(response.data);
        }).catch(function () {
        });
      }
    }
  };
</script>

<style lang="scss" scoped>
  .row1, .row2 {
    .item {
      display: inline-block;
      margin: 0 16px 8px 0;

      /deep/ .el-checkbox__label {
        padding-left: 5px;
      }
    }
  }
</style>
