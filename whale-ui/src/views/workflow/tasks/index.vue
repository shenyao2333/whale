<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" :placeholder="$t('flowModel.name')" style="width: 120px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button type="primary" icon="el-icon-search" class="filter-item" @click="handleFilter">查 询</el-button>
      <el-button type="primary" class="filter-item" icon="el-icon-refresh" @click="resetForm">重 置</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="objList"
      border
      style="width: 100%;">
      <el-table-column label="id" prop="id" align="center" >
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('flowModel.taskName')" prop="name" align="center" >
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('flowModel.assignee')" prop="assignee" align="center" >
        <template slot-scope="scope">
          <span>{{ scope.row.assignee }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('flowModel.description')" prop="description" align="center" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('flowModel.tenantId')" prop="tenantId" align="center" >
        <template slot-scope="scope">
          <span>{{ scope.row.tenantId }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('flowModel.createTime')" prop="createTime" align="center" >
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column width="250" fixed="right" label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="complete(scope.row)">办理</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.pageNo"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"/>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import BpmnDrawer from '@/views/workflow/bpmnDrawer.vue'

export default {
  name: 'Model',
  components: {
    Pagination,
    BpmnDrawer
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        T: 'success',
        F: 'danger'
      }
      return statusMap[status]
    }
  },
  data: function() {
    return {
      showCheckBox: true,
      editable: false,
      defaultChecked: { checkedpermissions: [], checkedrouters: [] },
      objList: [],
      dialogFormVisible: false,
      listLoading: true,
      total: 0,
      xmlmodel: undefined,
      listQuery: {
        pageNo: 1,
        pageSize: 20,
        createDate: undefined,
        latest: true
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.dialogFormVisible = false
      this.listLoading = true
    },
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },
    complete: function(item) {
      var params = {
        'withVariablesInReturn': true,
        'variables': {
          'aVariable': { 'value': 'aStringValue' },
          'anotherVariable': { 'value': 42 },
          'aThirdVariable': { 'value': true }}
      }


    },
    resetForm() {
      this.listQuery = {}
    },
    dateRangeChange(data) {
      if (data) {
        this.listQuery.start_time = data[0]
        this.listQuery.end_time = data[1]
      }
    }
  }
}
</script>
<style>
  .filter-container {
    padding-bottom: 10px;
  }
  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
  }
</style>
