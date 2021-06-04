<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">

      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="list" border>
      <!--<el-table-column label="序号"  type="index" width="50"/>-->
      <el-table-column label="用户编号" align="center" prop="user_id" width="80" />
      <el-table-column label="用户名称" align="center" prop="username" />
      <el-table-column label="令牌" align="center" prop="access_token" :show-overflow-tooltip="true" />
      <el-table-column label="类型" align="center" prop="token_type" />
      <el-table-column label="域" align="center" prop="scope" width="60" />
      <el-table-column label="过期时间" align="center" prop="expires_in" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPerm="['token_view']"
          >详细</el-button>
          <el-button
            v-if="scope.row.id !== 1"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            v-hasPerm="['token_del']"
          >删除</el-button>
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

    <!-- 令牌详细 -->
    <el-dialog title="令牌详细" :visible.sync="open" width="700px">
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户编号：">{{ form.user_id }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名称：">{{ form.username }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="令牌：">{{ form.access_token }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型：">{{ form.token_type }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="域：">{{ form.scope }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="过期时间：">{{ form.expires_in }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, delToken } from "@/api/monitor/token";

export default {
  name: "Token",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        current: 1,
        size: 10
      },
      // 显示搜索条件
      showSearch: true
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询令牌 */
    getList() {
      this.loading = true;
      list(this.queryParams).then( response => {
          this.list = response.data;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 1;
      this.getList();
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除按钮操作 */
    handleDel(row) {
      const token = row.access_token;
      this.$confirm('是否确认删除令牌为"' + token + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delToken(token);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    }
  }
};
</script>

