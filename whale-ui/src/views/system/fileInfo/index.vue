<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入文件名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件类别" prop="type">
        <el-select v-model="queryParams.type" placeholder="文件类别" clearable size="small">
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
          v-hasPerm="['fileInfo_add']"
        >上传</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDel"
          v-hasPerm="['fileInfo_del']"
        >删除</el-button>
      </el-col>
      <div class="top-right-btn">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
          <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
        </el-tooltip>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="fileInfoList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文件编号" align="center" prop="id" />
      <el-table-column label="文件名称" align="center" prop="name" :show-overflow-tooltip="true" >
        <template slot-scope="scope">
          <el-link :href="newPath(scope.row.path)" target="_blank">{{ scope.row.name}}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="缩略图" align="center" prop="thumbnail" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-link :href="newPath(scope.row.path)" target="_blank">
            <img v-if="scope.row.type == 'image'" :src="newPath(scope.row.path)" width="32" height="23"/>
            <i v-if="scope.row.type == 'media'"  class="el-icon-video-camera-solid" style="font-size: 23px"/>
            <i v-if="scope.row.type == 'file'" class="el-icon-copy-document" style="font-size: 23px"/>
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="文件类型" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column label="文件格式" align="center" prop="format" />
      <el-table-column label="文件大小" align="center" prop="size" />
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
            icon="el-icon-share"
            @click="handleShare(scope.row)"
            v-hasPerm="['fileInfo_share']"
          >分享</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDel(scope.row)"
            v-hasPerm="['fileInfo_del']"
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

    <!-- 添加或修改文件配置对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="500px">
      <el-upload
        ref="upload"
        :limit="5"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        multiple
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <!--<div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport" />七牛
        </div>-->
        <div class="el-upload__tip" style="color:red" slot="tip">提示：一次支持最多5个文件上传！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFileInfo, getFileInfo, delFileInfo, addFileInfo, editFileInfo } from "@/api/system/fileInfo";
import { getAccessToken } from "@/utils/auth";

export default {
  name: "FileInfo",
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
      // 文件表格数据
      fileInfoList: [],
      // 弹出层标题
      title: "",
      // 类型数据字典
      typeOptions: [],
      // 日期范围
      dateRange: [],
      // 查询文件
      queryParams: {
        current: 1,
        size: 10,
        name: undefined,
        type: undefined
      },
      // 显示搜索条件
      showSearch: true,
      // 文件上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getAccessToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/fileInfo/upload"
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("file_type").then(response => {
      this.typeOptions = response.data;
    });
  },
  methods: {
    /** 查询文件列表 */
    getList() {
      this.loading = true;
      listFileInfo(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.fileInfoList = response.data;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 文件系统内置字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
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
    /** 上传按钮操作 */
    handleAdd() {
      this.upload.title = "文件上传";
      this.upload.open = true;
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.data, "上传结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 分享按钮操作 */
    handleShare(row) {
      this.$alert('http://' + location.host + process.env.VUE_APP_BASE_API + row.path, "文件分享", { dangerouslyUseHTMLString: true });
    },
    /** 删除按钮操作 */
    handleDel(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除文件编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delFileInfo(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    }
  }
};
</script>
