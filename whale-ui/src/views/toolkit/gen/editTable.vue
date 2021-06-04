<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="basic">
        <basic-info-form ref="basicInfo" :info="info"/>
      </el-tab-pane>
      <el-tab-pane label="字段信息" name="cloum">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPerm="['datatable_add']"
            >新增
            </el-button>
          </el-col>

        </el-row>

        <el-table ref="dragTable" :data="cloumns" row-key="id" border :max-height="tableHeight">
          <el-table-column label="序号" type="index" min-width="5%" class-name="allowDrag"/>
          <el-table-column label="字段列名" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.columnName" placeholder="字段列名"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="字段描述" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.columnComment" placeholder="字段描述"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="物理类型" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.columnType" placeholder="物理类型"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="Java类型" min-width="11%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.javaType">
                <el-option label="Long" value="Long"/>
                <el-option label="String" value="String"/>
                <el-option label="Integer" value="Integer"/>
                <el-option label="Double" value="Double"/>
                <el-option label="BigDecimal" value="BigDecimal"/>
                <el-option label="Date" value="Date"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="默认值" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.defValue" placeholder="默认值"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="主键" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" false-label="0" v-model="scope.row.isPk"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="插入" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" false-label="0" v-model="scope.row.isAdd"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="编辑" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" false-label="0" v-model="scope.row.isEdit"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="列表" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" false-label="0" v-model="scope.row.isList"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" false-label="0" v-model="scope.row.isQuery"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询方式" min-width="10%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.queryType">
                <el-option label="=" value="eq"/>
                <el-option label="!=" value="ne"/>
                <el-option label=">" value="gt"/>
                <el-option label=">=" value="ge"/>
                <el-option label="<" value="lt"/>
                <el-option label="<=" value="le"/>
                <el-option label="in" value="in"/>
                <el-option label="like" value="like"/>
                <el-option label="isNull" value="isNull"/>
                <el-option label="isNotNull" value="isNotNull"/>
                <el-option label="between" value="between"/>
                <el-option label="notBetween" value="notBetween"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="必填" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox true-label="1" false-label="0" v-model="scope.row.isRequired"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="显示类型" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.htmlType">
                <el-option label="文本框" value="input"/>
                <el-option label="文本域" value="textarea"/>
                <el-option label="下拉框" value="select"/>
                <el-option label="单选框" value="radio"/>
                <el-option label="复选框" value="checkbox"/>
                <el-option label="日期控件" value="datetime"/>
                <el-option label="图片上传" value="imageUpload" />
                <el-option label="文件上传" value="fileUpload" />
                <el-option label="富文本控件" value="editor" />
                <el-option label="部门控件" value="dept"/>
                <el-option label="用户控件" value="user"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="字典类型" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.dictType" clearable filterable placeholder="请选择">
                <el-option
                  v-for="dict in dictOptions"
                  :key="dict.type"
                  :label="dict.name"
                  :value="dict.type">
                  <span style="float: left">{{ dict.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.type }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="80">
            <template slot-scope="scope">
              <svg-icon class="drag-handler" icon-class="drag" class-name="allowDrag"/>
              <el-button type="text" @click="handleDel(scope.$index, scope.row)" class="del-handler"
                         icon="el-icon-delete"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="生成信息" name="genInfo">
        <gen-info-form ref="genInfo" :info="info"/>
      </el-tab-pane>
    </el-tabs>
    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-100px;margin-top:10px;">
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="close()">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import {getGenTable, updateGenTable, menuTree} from "@/api/toolkit/datatable";
  import {dictList} from "@/api/system/dict";
  import basicInfoForm from "./basicInfoForm";
  import genInfoForm from "./genInfoForm";
  import Sortable from 'sortablejs'

  export default {
    name: "GenEdit",
    components: {
      basicInfoForm,
      genInfoForm
    },
    data() {
      return {
        // 选中选项卡的 name
        activeName: "basic",
        // 表格的高度
        tableHeight: document.documentElement.scrollHeight - 245 + "px",
        // 表列信息
        cloumns: [],
        // 字典信息
        dictOptions: [],
        // 删除表字段名
        delNames: [],
        // 菜单信息
        menus: [],
        // 表详细信息
        info: {}
      };
    },
    beforeCreate() {
      const {tableName, tableComment} = this.$route.query;
      // 获取表详细信息
      getGenTable(tableName, tableComment).then(res => {
        this.info = res.data;
        this.cloumns = res.data.columns;
        if (this.info.options) {
          const options = JSON.parse(this.info.options);
          this.info.treeId = options.treeId
          this.info.treeParentId = options.treeParentId
          this.info.treeName = options.treeName
        }
        this.info.menus = this.handleTree(res.data.menus, "id");
        this.info.tables = res.data.tables;
      });
      /** 查询字典下拉列表 */
      dictList().then(response => {
        this.dictOptions = response.data;
      });
    },
    mounted() {
      const el = this.$refs.dragTable.$el.querySelectorAll(".el-table__body-wrapper > table > tbody")[0];
      const sortable = Sortable.create(el, {
        handle: ".allowDrag",
        onEnd: evt => {
          const targetRow = this.cloumns.splice(evt.oldIndex, 1)[0];
          this.cloumns.splice(evt.newIndex, 0, targetRow);
          for (let index in this.cloumns) {
            this.cloumns[index].sort = parseInt(index) + 1;
          }
        }
      });
    },
    methods: {
      /** 提交按钮 */
      submitForm() {
        const basicForm = this.$refs.basicInfo.$refs.basicInfoForm
        const genForm = this.$refs.genInfo.$refs.genInfoForm
        Promise.all([basicForm, genForm].map(this.getFormPromise))
          .then(res => {
            const validateResult = res.every(item => !!item)
            if (validateResult) {
              const genTable = Object.assign({}, basicForm.model, genForm.model)
              //console.log(this.cloumns)
              //生成新sort
              for (let index in this.cloumns) {
                this.cloumns[index].sort = parseInt(index) + 1;
              }
              genTable.columns = this.cloumns
              genTable.params = {
                treeId: genTable.treeId,
                treeName: genTable.treeName,
                treeParentId: genTable.treeParentId
              }
              //console.log('delNames：' + JSON.stringify(this.delNames))
              if(this.delNames && this.delNames.length > 0){
                genTable.delNames = this.delNames.join()
              }
              //console.log('genTable：' + JSON.stringify(genTable))
              updateGenTable(genTable)
                .then(res => {
                  this.msgSuccess(res.msg)
                  if (res.code === 0) {
                    this.close()
                  }
                })
            } else {
              this.msgError('表单校验未通过，请重新检查提交内容')
            }
          })
      },
      getFormPromise(form) {
        return new Promise(resolve => {
          form.validate(res => {
            resolve(res);
          });
        });
      },
      handleAdd() {
        const cloumn = {
          columnName: '',
          columnComment: '',
          columnType: '',
          javaType: 'String',
          javaField: '',
          defValue: '',
          queryType: 'eq',
          htmlType: 'input',
          addData: true
        }
        this.cloumns.splice(this.cloumns.length - 6, 0, cloumn)
        for (let index in this.cloumns) {
          this.cloumns[index].sort = parseInt(index) + 1;
        }
      },
      /** 关闭按钮 */
      close() {
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({path: "/toolkit/datatable", query: {t: Date.now()}})
      },
      handleDel(index, row) {
        this.$confirm('是否确认删除字段列名为"' + row.columnName + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          if(row.id){
            this.delNames.push(row.columnName);
          }
          this.cloumns.splice(index, 1);
          this.msgSuccess("删除成功");
        }).catch(function (err) {
          console.log(err)
        });
      }
    }
  };
</script>
<style scoped>
  .drag-handler {
    width: 20px;
    height: 20px;
    cursor: pointer;
    color: #1890ff;
  }

  .del-handler {
    font-size: 20px;
    color: #ff4949;
  }
</style>
