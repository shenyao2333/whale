<template>
  <div class="app-container">
    <el-form :leave="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="申请人" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入请假名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请假类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择请假类型" clearable size="small">
          <el-option
            v-for="dict in typeOptions"
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
          v-hasPerm="['leave_add']"
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
          v-hasPerm="['leave_edit']"
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
          v-hasPerm="['leave_del']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPerm="['leave_export']"
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

    <el-table v-loading="loading" :data="leaveList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="请假id" align="center" prop="id"/>
      <el-table-column label="请假人" align="center" prop="userName" :show-overflow-tooltip="true"/>
      <el-table-column label="请假类型" align="center" prop="type" :formatter="typeFormat"/>
      <el-table-column label="请假事由" align="center" prop="cause" :show-overflow-tooltip="true"/>
      <el-table-column label="请假时间" align="center" prop="leaveTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.leaveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请假天数" align="center" prop="days"/>
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == 0 || scope.row.status == 8"
            size="mini"
            type="text"
            icon="el-icon-success"
            @click="handleSubmit(scope.row)"
            v-hasPerm="['leave_edit']">
            提交
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
            v-hasPerm="['leave_edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            v-hasPerm="['leave_del']"
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

    <!-- 添加或修改请假配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="请假类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择请假类型">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="请假时间" prop="leaveTime">
          <el-date-picker clearable size="small"
                          v-model="form.leaveTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择请假时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="请假天数" prop="days">
          <el-input-number v-model="form.days" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="请假事由" prop="cause">
          <el-input v-model="form.cause" type="textarea" rows="3" placeholder="请输入请假事由"/>
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
import {listLeave, getLeave, startProcess, delLeave, addLeave, exportLeave} from "@/api/activiti/leave";

export default {
  name: "Leave",
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
      // 请假表格数据
      leaveList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 请假类型数据字典
      typeOptions: [],
      // 状态字典
      statusOptions: [],
      // 查询请假
      queryParams: {
        current: 1,
        size: 10,
        userName: undefined,
        type: undefined
      },
      // 显示搜索条件
      showSearch: true,
      // 表单请假
      form: {},
      // 表单校验
      rules: {
        type: [
          {required: true, message: "请假类型不能为空", trigger: "blur"}
        ],
        leaveTime: [
          {required: true, message: "请假时间不能为空", trigger: "blur"}
        ],
        days: [
          {required: true, message: "请假天数不能为空", trigger: "blur"}
        ],
        cause: [
          {required: true, message: "请假事由不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("leave_type").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("leave_status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询请假列表 */
    getList() {
      this.loading = true;
      listLeave(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.leaveList = response.data;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 请假类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 状态字典翻译
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
        type: undefined,
        leaveTime: undefined,
        days: 1,
        cause: undefined
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加请假";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleSubmit: function (row, index) {
      this.$confirm('是否确认提交ID为' + row.id + '的数据项?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return startProcess(row.id)
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 请假按钮操作 */
    handleEdit(row) {
      this.reset();
      const id = row.id || this.ids
      getLeave(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改请假";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            editLeave(this.form).then(response => {
              if (response.code === 0) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addLeave(this.form).then(response => {
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
      this.$confirm('是否确认删除请假编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delLeave(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有请假数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportLeave(queryParams);
      }).then(response => {
        console.log(response.data)
        this.download(response.data);
      }).catch(function () {
      });
    },
  }
};
</script>
