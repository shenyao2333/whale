<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="控件Label" prop="controlLabel">
        <el-input
          v-model="queryParams.controlLabel"
          placeholder="请输入控件Label"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="控件图标" prop="controlTagIcon">
        <el-input
          v-model="queryParams.controlTagIcon"
          placeholder="请输入控件图标"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
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

    <el-table v-loading="loading" :data="controlsList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="控件Label" align="center" prop="controlLabel" width="90"/>
      <el-table-column label="控件图标" align="center" prop="controlTagIcon" width="90"/>
      <el-table-column label="控件表名" align="center" prop="controlMappingTable" width="120"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="控件字段类型" align="center" prop="controlFieldType" width="120"/>
      <el-table-column label="控件字段长度" align="center" prop="controlFieldLength" width="120"/>
      <el-table-column label="控件详细属性" align="center" prop="controlAttribute" width="180" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" width="180" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
          >修改
          </el-button>
          <el-button
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

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="控件Label" prop="controlLabel">
          <el-input v-model="form.controlLabel" placeholder="请输入控件Label"/>
        </el-form-item>
        <el-form-item label="控件图标" prop="controlTagIcon">
          <el-popover
            placement="bottom-start"
            width="460"
            trigger="click"
            @show="$refs['iconSelect'].reset()"
          >
            <IconSelect ref="iconSelect" @selected="selected"/>
            <el-input slot="reference" v-model="form.controlTagIcon" placeholder="点击选择图标" readonly>
              <svg-icon
                v-if="form.controlTagIcon"
                slot="prefix"
                :icon-class="form.controlTagIcon"
                class="el-input__icon"
                style="height: 32px;width: 16px;"
              />
              <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
            </el-input>
          </el-popover>
        </el-form-item>
        <el-form-item label="控件表名后缀" prop="controlMappingTableSuffix">
          <el-input v-model="form.controlMappingTableSuffix" placeholder="请输入控件表名">
            <template slot="prepend">{{controlMappingTablePrepend}}</template>
          </el-input>
        </el-form-item>
        <el-form-item label="控件字段类型" prop="controlFieldType">
          <el-input v-model="form.controlFieldType" placeholder="请输入控件字段类型"/>
        </el-form-item>
        <el-form-item label="控件字段长度" prop="controlFieldLength">
          <el-slider v-model="form.controlFieldLength" :min="0" :max="9999" show-input placeholder="请输入控件字段长度"/>
        </el-form-item>
        <el-form-item label="控件字段精度" prop="controlFieldLength">
          <el-slider v-model="form.controlPrecision" :min="0" :max="9999" show-input placeholder="请输入控件字段精度"/>
        </el-form-item>
        <el-form-item label="控件详细属性" prop="controlAttribute">
          <el-input :disabled="controlAttributeDisabled" v-model="form.controlAttribute"
                    :autosize="{ minRows: 8, maxRows: 14}" type="textarea" placeholder="控件详细属性"/>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" :autosize="{ minRows: 4, maxRows: 4}" type="textarea" placeholder="请输入备注"/>
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
import {
  listControlSteward,
  addControlSteward,
  delControlSteward,
  editControlSteward,
  getControlSteward
} from '@/api/intelligent/controlSteward'
import IconSelect from "@/components/IconSelect";


export default {
  name: "controlSteward",
  components: {IconSelect},
  data() {
    return {
      controlAttributeDisabled: false,
      testKey: 0,
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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表格绑定数据
      controlsList: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        controlLabel: undefined,
        controlTagIcon: undefined
      },
      // 显示搜索条件
      showSearch: true,
      // 表单参数
      form: {},
      controlMappingTablePrepend: 'intelligent_controls_',
      // 表单校验
      rules: {
        controlLabel: [
          {required: true, message: "控件Label不能为空", trigger: "blur"}
        ],
        controlTagIcon: [
          {required: true, message: "控件图标不能为空", trigger: "blur"}
        ],
        controlMappingTableSuffix: [
          {required: true, message: "控件表名后缀不能为空", trigger: "blur"}
        ],
        controlFieldType: [
          {required: true, message: "控件字段类型不能为空", trigger: "blur"}
        ],
        controlFieldLength: [
          {required: true, message: "控件字段长度不能为空", trigger: "blur"}
        ],
        controlAttribute: [
          {required: true, message: "控件详细属性不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.controlTagIcon = name;
      this.$refs["form"].validateField("controlTagIcon");
    },
    /** 查询控件大管家列表 */
    getList() {
      this.loading = true;
      listControlSteward(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.controlsList = response.data;
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
      this.form = {
        controlLabel: undefined,
        controlTagIcon: undefined,
        controlMappingTableSuffix: undefined,
        controlFieldType: undefined,
        controlFieldLength: 0,
        controlPrecision: 0,
        controlAttribute: undefined,
        remarks: undefined
      }
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
      this.reset()
      this.controlAttributeDisabled = false
      this.open = true
      this.title = "添加控件模板"
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      this.reset()
      const id = row.id || this.ids
      getControlSteward(id).then(response => {
        this.form = response.data
        this.controlAttributeDisabled = true
        this.open = true
        this.title = "修改控件模板"
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            editControlSteward({
              ...this.form,
              controlMappingTablePrepend: this.controlMappingTablePrepend
            }).then(response => {
              if (response.code === 0) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addControlSteward({
              ...this.form,
              controlMappingTablePrepend: this.controlMappingTablePrepend
            }).then(response => {
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
      this.$confirm('是否确认删除控件编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delControlSteward(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    }
  }
};
</script>


<style lang="scss" scoped>

</style>
