<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户昵称" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入用户昵称"
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
    <el-table v-loading="loading" :data="demoUserList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="45" align="center"/>
      <el-table-column label="用户编号" align="center" prop="id"/>
      <el-table-column label="用户昵称" align="center" prop="nickName"/>
      <el-table-column label="手机号码" align="center" prop="phone"/>
      <el-table-column label="备注" align="center" prop="remarks" :show-overflow-tooltip="true"/>
      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
          >修改
          </el-button>
          <el-button
            v-if="scope.row.id !== 1"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
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

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="720px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入用户昵称"/>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号码" maxlength="11"/>
        </el-form-item>
        <component-integrated :form-code="formCode" :form-data="form" :visible="open"/>
        <el-form-item label="备注">
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listDemoUser, getDemoUser, addDemoUser, editDemoUser, delDemoUser} from "@/api/intelligent/demoUser";
import componentIntegrated from '@/components/FormExpandDesign/FormIntegrated/ComponentIntegrated'
import {resetFormFields, makeIntegrated, mergeFormData} from "@/utils/formExpandDesign/formIntegrated";

export default {
  name: "User",
  components: {componentIntegrated},
  data() {
    return {
      formCode: "form1",
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
      // 用户表格数据
      demoUserList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单参数||合集
      form: {},
      // 自定义表单参数
      customizeForm: {
        nickName: undefined,
        phone: undefined,
        remarks: undefined
      },
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        nickName: undefined
      },
      // 显示搜索条件
      showSearch: true,
      // 表单校验||合集
      rules: {},
      // 自定义表单校验
      customizeRules: {
        nickName: [
          {required: true, message: "用户昵称不能为空", trigger: "blur"}
        ],
        phone: [
          {required: true, message: "手机号码不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listDemoUser(this.queryParams).then(response => {
          this.demoUserList = response.data;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      resetFormFields(this.form, this.customizeForm)
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.form = {}
      this.rules = {}
      mergeFormData(this.formCode, this.form, this.rules, this.customizeForm, this.customizeRules)
        .then(() => {
          this.reset()
          this.open = true;
          this.title = "添加用户模板";
        })
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      this.form = {}
      this.rules = {}
      mergeFormData(this.formCode, this.form, this.rules, this.customizeForm, this.customizeRules)
        .then(() => {
          const id = row.id || this.ids;
          getDemoUser(id, this.formCode).then(response => {
            for (const key in response.data) {
              this.$set(this.form, key, response.data[key])
            }
            this.open = true;
            this.title = "修改用户模板";
          });
        })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            editDemoUser(makeIntegrated(this.formCode, this.form)).then(response => {
              if (response.code === 0) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addDemoUser(makeIntegrated(this.formCode, this.form)).then(response => {
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
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除用户编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return delDemoUser(ids, this.formCode);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      });
    }
  }
};
</script>
