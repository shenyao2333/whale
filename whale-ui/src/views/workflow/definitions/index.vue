<template>
  <div class="app-container">
    <BpmnDrawer v-if="dialogFormVisible" ref="bpmndrawer" @close="dialogFormVisible=false" @exportXML="exportXML"/>
    <div v-else>
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

        <el-table-column :label="$t('flowModel.name')" prop="name" align="center" >
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('flowModel.key')" prop="createDate" align="center" width="160">
          <template slot-scope="scope">
            <span>{{ scope.row.key }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('flowModel.tenantId')" prop="tenantId" align="center" >
          <template slot-scope="scope">
            <span>{{ scope.row.tenantId }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('flowModel.version')" prop="version" align="center" >
          <template slot-scope="scope">
            <span>{{ scope.row.version }}</span>
          </template>
        </el-table-column>
        <el-table-column width="250" fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleAddOrEdit(scope.row)">绘图</el-button>
            <el-button size="mini" type="primary" @click="startFlow(scope.row)">发起流程</el-button>
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
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import BpmnDrawer from '@/views/workflow/bpmnDrawer.vue'
import blankbpmn from '../blankbpmn'

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
      var _this = this
      definitionsService.count(this.listQuery).then(function(cres) {
        _this.listQuery.firstResult = (_this.listQuery.pageNo - 1) * _this.listQuery.pageSize
        _this.listQuery.maxResults = _this.listQuery.pageNo * _this.listQuery.pageSize
        definitionsService.list(_this.listQuery).then(res => {
          _this.objList = res
          _this.total = cres.count
          _this.listLoading = false
        }, err => {
          console.log(err)
          _this.listLoading = false
        })
      })
    },
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },
    startFlow: function(item) {
      var params = {
        'processDefinitionId': item.id,
        'businessKey': 'myBusinessKey',
        'returnVariables': true,
        'variables': { 'employee': {
          'value': 'aStringValue',
          'type': 'String'
        }}
      }
      processService.start(params).then(function(res) {
        console.log(res)
      })
    },
    resetForm() {
      this.listQuery = {}
    },
    dateRangeChange(data) {
      if (data) {
        this.listQuery.start_time = data[0]
        this.listQuery.end_time = data[1]
      }
    },
    handleAddOrEdit(item) {
      var _this = this
      this.dialogFormVisible = true
      if (item) {
        definitionsService.getBPMNXML(item.id).then(function(res) {
          _this.$nextTick(() => {
            _this.$refs.bpmndrawer.initCanvas(res.bpmn20Xml, item.id, item.name)
          })
        })
      } else {
        _this.$nextTick(() => {
          _this.$refs.bpmndrawer.initCanvas(blankbpmn, item.id.item.name)
        })
      }
    },
    exportXML: function(xml, filename, modelId) {
      var _this = this
      deploymentService.deployment(xml, filename, _this.$store.getters.name).then(function(res) {
        _this.getList()
      })
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
