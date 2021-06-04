<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="角色名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入角色名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="角色状态"
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
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPerm="['role_add']"
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
          v-hasPerm="['role_edit']"
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
          v-hasPerm="['role_del']"
        >删除
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

    <el-table v-loading="loading" :data="roleList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="角色编号" prop="id" width="120"/>
      <el-table-column label="角色名称" prop="name" :show-overflow-tooltip="true" width="150"/>
      <el-table-column label="显示顺序" prop="sort" width="100"/>
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
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
            v-hasPerm="['role_edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleDataScope(scope.row)"
            v-hasPerm="['role_data']"
          >数据权限
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            v-hasPerm="['role_del']"
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

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称"/>
        </el-form-item>
        <el-form-item label="角色顺序" prop="sort">
          <el-input-number v-model="form.sort" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="所属应用" prop="applications">
          <el-checkbox-group v-model="form.applications"  @change="applicationsChange">
            <el-checkbox v-for="application in applicationOptions" :label="application.id" :key="application.id" name="applications">{{application.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="角色权限">
          <el-tree
            :data="menuOptions"
            show-checkbox
            ref="menu"
            node-key="id"
            empty-text="暂无数据"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
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
        <el-form-item label="备注">
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色数据权限对话框 -->
    <el-dialog :title="title" :visible.sync="openDataScope" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="form.name" :disabled="true"/>
        </el-form-item>
        <el-form-item label="权限代码">
          <el-input v-model="form.code" :disabled="true"/>
        </el-form-item>
        <el-form-item label="权限范围">
          <el-select v-model="form.dataScope">
            <el-option
              v-for="item in dataScopeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据权限" v-show="form.dataScope == 2">
          <el-tree
            :data="deptOptions"
            show-checkbox
            default-expand-all
            ref="dept"
            node-key="id"
            empty-text="加载中，请稍后"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">确 定</el-button>
        <el-button @click="cancelDataScope">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {listRole, getRole, delRole, addRole, editRole, exportRole, dataScope, changeStatus} from "@/api/system/role";
  import {menuTree, roleMenuTree} from "@/api/system/menu";
  import {deptTree, roleDeptTree} from "@/api/system/dept";
  import {listApplication} from "@/api/system/application";

  export default {
    name: "Role",
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
        // 角色表格数据
        roleList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否显示弹出层（数据权限）
        openDataScope: false,
        // 日期范围
        dateRange: [],
        // 状态数据字典
        statusOptions: [],
        applicationOptions: [],
        // 数据范围选项
        dataScopeOptions: [
          {
            value: "1",
            label: "全部数据权限"
          },
          {
            value: "2",
            label: "自定数据权限"
          },
          {
            value: "3",
            label: "本机构数据权限"
          },
          {
            value: "4",
            label: "本机构及以下数据权限"
          },
          {
            value: "5",
            label: "仅本人数据权限"
          }
        ],
        // 菜单列表
        menuOptions: [],
        // 机构列表
        deptOptions: [],
        // 查询参数
        queryParams: {
          current: 1,
          size: 10,
          name: undefined,
          code: undefined,
          status: undefined
        },
        // 显示搜索条件
        showSearch: true,
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "name"
        },
        // 表单校验
        rules: {
          name: [
            {required: true, message: "角色名称不能为空", trigger: "blur"}
          ],
          sort: [
            {required: true, message: "角色顺序不能为空", trigger: "blur"}
          ]
        }
      };
    },
    created() {
      this.getList();
      this.getAppList();
      this.getDicts("status").then(response => {
        this.statusOptions = response.data;
      });

    },
    methods: {
      /** 查询角色列表 */
      getList() {
        this.loading = true;
        listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
          response => {
            this.roleList = response.data;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 查询应用列表 */
      getAppList() {
        this.loading = true;
        listApplication({current: 1, size: 30}).then(response => {
            this.applicationOptions = response.data.map(function (item) {
              item.id = 'app' + item.id;
              return item;
            });
            this.loading = false;
          }
        );
      },
      /** 查询菜单树结构 */
      getMenuTree() {
        var applicationIds = ''
        if(this.form.applications.length > 0){
          applicationIds = this.form.applications.map(function (item) {
            return item.replace('app', '')
          }).join()
        }
        menuTree({applicationIds: applicationIds}).then(response => {
          this.menuOptions = this.handleTree(response.data, "id", "parentId");
        });
      },
      /** 查询机构树结构 */
      getDeptTree() {
        deptTree().then(response => {
          this.deptOptions = response.data;
        });
      },
      // 所有菜单节点数据
      getMenuAllCheckedKeys() {
        // 目前被选中的菜单节点
        let checkedKeys = this.$refs.menu.getHalfCheckedKeys();
        // 半选中的菜单节点
        let halfCheckedKeys = this.$refs.menu.getCheckedKeys();
        checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
        return checkedKeys;
      },
      // 所有机构节点数据
      getDeptAllCheckedKeys() {
        // 半选中的机构节点
        let halfCheckedKeys = this.$refs.dept.getCheckedKeys();
        return halfCheckedKeys;
      },
      /** 根据角色ID查询菜单树结构 */
      getRoleMenuTree(id) {
        roleMenuTree(id).then(response => {
          this.menuOptions = this.handleTree(response.data.result, "id", "parentId");
          this.$refs.menu.setCheckedKeys(response.data.extend);
        });
      },
      /** 根据角色ID查询机构树结构 */
      getRoleDeptTree(id) {
        roleDeptTree(id).then(response => {
          this.deptOptions = response.data.result;
          this.$refs.dept.setCheckedKeys(response.data.extend);
        });
      },
      // 角色状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$confirm('确认要"' + text + '""' + row.name + '"角色吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return changeStatus(row.id, row.status);
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 取消按钮（数据权限）
      cancelDataScope() {
        this.openDataScope = false;
        this.reset();
      },
      // 表单重置
      reset() {
        if (this.$refs.menu != undefined) {
          this.$refs.menu.setCheckedKeys([]);
        }
        this.form = {
          id: undefined,
          name: undefined,
          sort: 0,
          status: "0",
          menuIds: [],
          deptIds: [],
          applications: ['app1'],
          remarks: undefined
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
        this.dateRange = [];
        this.resetForm("queryForm");
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.getMenuTree();
        this.open = true;
        this.title = "添加角色";
      },
      /** 修改按钮操作 */
      handleEdit(row) {
        this.reset();
        const id = row.id || this.ids
        this.$nextTick(() => {
          this.getRoleMenuTree(id);
        });
        getRole(id).then(response => {
          this.form = response.data;
          this.form.applications = this.form.applications.split(',')
          this.open = true;
          this.title = "修改角色";
        });
      },
      /** 分配数据权限操作 */
      handleDataScope(row) {
        this.reset();
        this.$nextTick(() => {
          this.getRoleDeptTree(row.id);
        });
        getRole(row.id).then(response => {
          this.form = response.data;
          this.openDataScope = true;
          this.title = "分配数据权限";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.menuIds = this.getMenuAllCheckedKeys();
            this.form.applications = this.form.applications.join()
            console.log(this.form.applications)
            if (this.form.id != undefined) {
              editRole(this.form).then(response => {
                if (response.code === 0) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addRole(this.form).then(response => {
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
      /** 提交按钮（数据权限） */
      submitDataScope: function () {
        if (this.form.id != undefined) {
          this.form.deptIds = this.getDeptAllCheckedKeys();
          dataScope(this.form).then(response => {
            if (response.code === 0) {
              this.msgSuccess("修改成功");
              this.openDataScope = false;
              this.getList();
            } else {
              this.msgError(response.msg);
            }
          });
        }
      },
      /** 删除按钮操作 */
      handleDel(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除角色编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delRole(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      applicationsChange(){
        console.log(JSON.stringify(this.form.applications))
        this.getMenuTree();
      }
    }
  };
</script>
