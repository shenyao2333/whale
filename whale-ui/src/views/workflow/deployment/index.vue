<template>
  <div class="app-container">
    <BpmnDrawer v-if="showDraw" ref="bpmndrawer" @close="showDraw=false" @exportXML="exportXML"/>
    <div v-else>
      <div class="filter-container">
        <el-input v-model="listQuery.name" :placeholder="$t('flowModel.name')" style="width: 120px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-button type="primary" icon="el-icon-search" class="filter-item" @click="handleFilter">查 询</el-button>
        <el-button type="primary" icon="el-icon-edit" class="filter-item" @click="handleAddOrEdit()">新  增</el-button>
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
        <el-table-column :label="$t('flowModel.tenantId')" prop="tenantId" align="center" >
          <template slot-scope="scope">
            <span>{{ scope.row.tenantId }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('flowModel.source')" prop="source" align="center" >
          <template slot-scope="scope">
            <span>{{ scope.row.source }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('flowModel.deploymentId')" prop="deploymentId" align="center" >
          <template slot-scope="scope">
            <span>{{ scope.row.id |deployedFilter }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('flowModel.deploymentTime')" prop="deploymentTime" align="center" >
          <template slot-scope="scope">
            <span>{{ scope.row.deploymentTime }}</span>
          </template>
        </el-table-column>
        <el-table-column width="250" fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="success" icon="el-icon-picture-outline" circle title="绘图" @click="drawBpmn(scope.row)"/>
            <el-button v-if="scope.row.deploymentId==null&&scope.row.sourceUrl!=null" size="mini" type="primary" icon="el-icon-upload" circle title="部署" @click="deployModel(scope.row)"/>
            <el-button size="mini" type="danger" icon="el-icon-delete" circle title="删除" @click="deleteModel(scope.row)"/>

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
import blankbpmn from '../blankbpmn'
import ModelCreateUpdate from './create_update'
import BpmnDrawer from '@/views/workflow/bpmnDrawer.vue'

export default {
  name: 'Model',
  components: {
    Pagination,
    BpmnDrawer,
    ModelCreateUpdate
  },
  filters: {
    deployedFilter(id) {
      if (id) {
        return id
      }
      return '未部署'
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
      showDraw: false,
      xmlmodel: undefined,
      listQuery: {
        pageNo: 1,
        pageSize: 10,
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
      this.listLoading = true
      var _this = this
      deploymentService.count(this.listQuery).then(function(cres) {
        _this.listQuery.firstResult = (_this.listQuery.pageNo - 1) * _this.listQuery.pageSize
        _this.listQuery.maxResults = _this.listQuery.pageNo * _this.listQuery.pageSize
        deploymentService.list(_this.listQuery).then(res => {
          _this.total = cres.count
          _this.objList = res
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
    startFlow: function() {},
    resetForm() {
      this.listQuery = {}
    },
    dateRangeChange(data) {
      if (data) {
        this.listQuery.start_time = data[0]
        this.listQuery.end_time = data[1]
      }
    },
    deployModel: function(item) {
      var _this = this
      modelService.getEditorSrc(item.id).then(function(xml) {
        deploymentService.deployment(xml, item.name + '.bpmn20.xml', _this.$store.getters.name).then(function(res) {
          /* 获取流程部署id*/
          /* 版本加1*/
          var params = Object.assign({}, item)
          params.deploymentId = res.id
          params.version = params.version + 1
          modelService.save(params)
          _this.getList()
        })
      })
    },
    drawBpmn(item) {
      var _this = this
      this.showDraw = true
      /* 如果有部署id说明部署过*/

      deploymentService.resources(item.id).then(function(res) {
        request({ url: '/flow/rest/deployment/' + item.id + '/resources/' + res[0].id + '/data', method: 'get' }).then(function(xml) {
          _this.$nextTick(() => {
            _this.$refs.bpmndrawer.initCanvas(xml, item.id, item.name)
          })
        })
      })
    },
    handleAddOrEdit() {
      this.showDraw = true
      this.$nextTick(() => {
        this.$refs.bpmndrawer.initCanvas(blankbpmn)
      })
    },
    exportXML: function(xml, filename, id) {
      var _this = this
      deploymentService.deployment(xml, filename, _this.$store.getters.name).then(function(res) {
        _this.showDraw = false
        _this.getList()
      })
    },
    deleteModel: function(item) {
      var _this = this
      deploymentService.delete(item.id).then(function() {
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
