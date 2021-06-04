<template>
  <div class="right-board">
    <!--Tabs标签页切换面板-->
    <el-tabs v-model="currentTab" class="center-tabs">
      <el-tab-pane label="组件属性" name="field"/>
      <el-tab-pane label="表单配置" name="formConf"/>
    </el-tabs>

    <!--控件属性操作面板-->
    <div class="field-box">
      <a class="document-link" target="_blank" :href="documentLink" title="查看组件文档">
        <i class="el-icon-link"/>
      </a>
      <el-scrollbar class="right-scrollbar">
        <!-- 控件属性(属性公用) -->
        <el-form v-show="currentTab==='field' && showField" size="small" label-width="90px">

          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'config.changeTag')" label="组件类型">
            <el-select
              :value="activeData.config.tagIcon"
              placeholder="请选择组件类型"
              :style="{width: '100%'}"
              @change="tagChange"
            >
              <el-option-group v-for="group in tagList" :key="group.label" :label="group.label">
                <el-option
                  v-for="item in group.options"
                  :key="item.config.label"
                  :label="item.config.label"
                  :value="item.config.tagIcon"
                >
                  <svg-icon class="node-icon" :icon-class="item.config.tagIcon"/>
                  <span> {{ item.config.label }}</span>
                </el-option>
              </el-option-group>
            </el-select>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.vModel')!==undefined" label="字段名">
            <el-input v-model="activeData.config.vModel" placeholder="请输入字段名(v-model)"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.componentName')!==undefined" label="组件名">
            {{ activeData.config.componentName }}
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.label')!==undefined" label="标题">
            <el-input v-model="activeData.config.label" placeholder="请输入标题"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.placeholder')!==undefined" label="占位提示">
            <el-input v-model="activeData.attrs.placeholder" placeholder="请输入占位提示"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.start-placeholder')!==undefined" label="开始占位">
            <el-input v-model="activeData.attrs['start-placeholder']" placeholder="请输入占位提示"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.end-placeholder')!==undefined" label="结束占位">
            <el-input v-model="activeData.attrs['end-placeholder']" placeholder="请输入占位提示"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.span')!==undefined" label="表单栅格">
            <el-slider v-model="activeData.config.span" :max="24" :min="1" :marks="{12:''}"/>
          </el-form-item>

          <!--行容器-->
          <el-form-item v-if="this.vGet(activeData,'config.rowHeight')!==undefined " label="调整行高">
            <el-slider v-model="activeData.config.rowHeight" :min="0" :max="1000" show-input/>
          </el-form-item>
          <el-form-item
            v-if="this.vGet(activeData,'config.layout')==='rowFormItem' && this.vGet(activeData,'config.gutter')!==undefined"
            label="栅格间隔">
            <el-input-number v-model="activeData.config.gutter" :min="0" placeholder="栅格间隔"/>
          </el-form-item>
          <el-form-item
            v-if="this.vGet(activeData,'config.layout')==='rowFormItem' && this.vGet(activeData,'attrs.type')!==undefined"
            label="布局模式">
            <el-radio-group v-model="activeData.attrs.type">
              <el-radio-button label="default"/>
              <el-radio-button label="flex"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            v-if="this.vGet(activeData,'attrs.justify')!==undefined && this.vGet(activeData,'attrs.type')==='flex'"
            label="水平排列">
            <el-select v-model="activeData.attrs.justify" placeholder="请选择水平排列" :style="{width: '100%'}">
              <el-option
                v-for="(item, index) in justifyOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="this.vGet(activeData,'attrs.align')!==undefined && this.vGet(activeData,'attrs.type')==='flex'"
            label="垂直排列">
            <el-radio-group v-model="activeData.align">
              <el-radio-button label="top"/>
              <el-radio-button label="middle"/>
              <el-radio-button label="bottom"/>
            </el-radio-group>
          </el-form-item>

          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'config.labelWidth')!==undefined" label="标签宽度">
            <el-input v-model.number="activeData.config.labelWidth" type="number" placeholder="请输入标签宽度"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'style.width')!==undefined" label="组件宽度">
            <el-input v-model="activeData.style.width" placeholder="请输入组件宽度" clearable/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.vModel')!==undefined" label="默认值">
            <el-input
              :value="setDefaultValue(activeData.config.defaultValue)"
              placeholder="请输入默认值"
              @input="onDefaultValueInput"
            />
          </el-form-item>

          <!--多选框组-->
          <el-form-item v-if="this.vGet(activeData,'config.tag')==='el-checkbox-group'" label="至少应选">
            <el-input-number
              :value="activeData.attrs.min"
              :min="0"
              placeholder="至少应选"
              @input="$set(activeData, 'attrs.min', $event?$event:undefined)"
            />
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.tag')==='el-checkbox-group'" label="最多可选">
            <el-input-number
              :value="activeData.attrs.max"
              :min="0"
              placeholder="最多可选"
              @input="$set(activeData.attrs,'max', $event?$event:undefined)"
            />
          </el-form-item>

          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'slot.prepend')!==undefined" label="前缀">
            <el-input v-model="activeData.slot.prepend" placeholder="请输入前缀"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'slot.append')!==undefined" label="后缀">
            <el-input v-model="activeData.slot.append" placeholder="请输入后缀"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.prefix-icon')!==undefined" label="前图标">
            <el-input v-model="activeData.attrs['prefix-icon']" placeholder="请输入前图标名称">
              <el-button slot="append" icon="el-icon-thumb" @click="openIconsDialog('prefix-icon')">
                选择
              </el-button>
            </el-input>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.suffix-icon')!== undefined" label="后图标">
            <el-input v-model="activeData.attrs['suffix-icon']" placeholder="请输入后图标名称">
              <el-button slot="append" icon="el-icon-thumb" @click="openIconsDialog('suffix-icon')">
                选择
              </el-button>
            </el-input>
          </el-form-item>

          <!--级联选择-->
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-cascader'" label="选项分隔符">
            <el-input v-model="activeData.attrs.separator" placeholder="请输入选项分隔符"/>
          </el-form-item>

          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'attrs.autosize')!== undefined" label="最小行数">
            <el-input-number v-model="activeData.attrs.autosize.minRows" :min="1" placeholder="最小行数"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.autosize')!== undefined" label="最大行数">
            <el-input-number v-model="activeData.attrs.autosize.maxRows" :min="1" placeholder="最大行数"/>
          </el-form-item>
          <el-form-item v-if="isShowMin" label="最小值">
            <el-input-number v-model="activeData.attrs.min" placeholder="最小值"/>
          </el-form-item>
          <el-form-item v-if="isShowMax" label="最大值">
            <el-input-number v-model="activeData.attrs.max" placeholder="最大值"/>
          </el-form-item>
          <el-form-item v-if="isShowStep" label="步长">
            <el-input-number v-model="activeData.attrs.step" placeholder="步数"/>
          </el-form-item>

          <!--计数器-->
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-input-number'" label="精度">
            <el-input-number v-model="activeData.attrs.precision" :min="0" placeholder="精度"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-input-number'" label="按钮位置">
            <el-radio-group v-model="activeData.attrs['controls-position']">
              <el-radio-button label="">
                默认
              </el-radio-button>
              <el-radio-button label="right">
                右侧
              </el-radio-button>
            </el-radio-group>
          </el-form-item>

          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'attrs.maxlength') !== undefined" label="最多输入">
            <el-input v-model="activeData.attrs.maxlength" placeholder="请输入字符长度">
              <template slot="append">
                个字符
              </template>
            </el-input>
          </el-form-item>

          <!--开关-->
          <el-form-item v-if="this.vGet(activeData,'attrs.active-text') !== undefined" label="开启提示">
            <el-input v-model="activeData.attrs['active-text']" placeholder="请输入开启提示"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.inactive-text') !== undefined" label="关闭提示">
            <el-input v-model="activeData.attrs['inactive-text']" placeholder="请输入关闭提示"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.active-value') !== undefined" label="开启值">
            <el-input
              :value="setDefaultValue(activeData.attrs['active-value'])"
              placeholder="请输入开启值"
              @input="onSwitchValueInput($event, 'active-value')"
            />
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.inactive-value') !== undefined" label="关闭值">
            <el-input
              :value="setDefaultValue(activeData.attrs['inactive-value'])"
              placeholder="请输入关闭值"
              @input="onSwitchValueInput($event, 'inactive-value')"
            />
          </el-form-item>

          <!--公用属性-->
          <el-form-item
            v-if="this.vGet(activeData,'attrs.type') !== undefined && 'el-date-picker' === this.vGet(activeData,'config.tag')"
            label="时间类型"
          >
            <el-select
              v-model="activeData.attrs.type"
              placeholder="请选择时间类型"
              :style="{ width: '100%' }"
              @change="dateTypeChange"
            >
              <el-option
                v-for="(item, index) in dateOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>


          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'attrs.range-separator') !== undefined" label="分隔符">
            <el-input v-model="activeData.attrs['range-separator']" placeholder="请输入分隔符"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.picker-options') !== undefined" label="时间段">
            <el-input
              v-model="activeData.attrs['picker-options'].selectableRange"
              placeholder="请输入时间段"
            />
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.format') !== undefined" label="时间格式">
            <el-input
              :value="activeData.attrs.format"
              placeholder="请输入时间格式"
              @input="setTimeValue($event)"
            />
          </el-form-item>

          <!--公用选项模板-->
          <template
            v-if="['el-checkbox-group', 'el-radio-group', 'el-select'].indexOf(this.vGet(activeData,'config.tag')) > -1">
            <el-divider>选项</el-divider>
            <draggable
              :list="activeData.slot.options"
              :animation="340"
              group="selectItem"
              handle=".option-drag"
            >
              <div v-for="(item, index) in activeData.slot.options" :key="index" class="select-item">
                <div class="select-line-icon option-drag">
                  <i class="el-icon-s-operation"/>
                </div>
                <el-input v-model="item.label" placeholder="选项名" size="small"/>
                <el-input
                  placeholder="选项值"
                  size="small"
                  :value="item.value"
                  @input="setOptionValue(item, $event)"
                />
                <div class="close-btn select-line-icon" @click="activeData.slot.options.splice(index, 1)">
                  <i class="el-icon-remove-outline"/>
                </div>
              </div>
            </draggable>
            <div style="margin-left: 20px;">
              <el-button
                style="padding-bottom: 0"
                icon="el-icon-circle-plus-outline"
                type="text"
                @click="addSelectItem"
              >
                添加选项
              </el-button>
            </div>
            <el-divider/>
          </template>

          <!--级联选项模板-->
          <template v-if="['el-cascader'].indexOf(this.vGet(activeData,'config.tag')) > -1">
            <el-divider>选项</el-divider>
            <el-form-item label="数据类型">
              <el-radio-group v-model="activeData.config.dataType" size="small">
                <el-radio-button label="dynamic">
                  动态数据
                </el-radio-button>
                <el-radio-button label="static">
                  静态数据
                </el-radio-button>
              </el-radio-group>
            </el-form-item>

            <template v-if="activeData.config.dataType === 'dynamic'">
              <el-form-item label="接口地址">
                <el-input
                  v-model="activeData.config.url"
                  placeholder="请输入接口地址"
                  clearable>
                  <el-select
                    slot="prepend"
                    v-model="activeData.config.method"
                    :style="{width: '85px'}">
                    <el-option label="get" value="get"/>
                    <el-option label="post" value="post"/>
                    <el-option label="put" value="put"/>
                    <el-option label="delete" value="delete"/>
                  </el-select>
                </el-input>
              </el-form-item>
              <el-form-item label="数据位置">
                <el-input v-model="activeData.config.dataKey" placeholder="请输入标签键名"/>
              </el-form-item>
              <el-form-item label="标签键名">
                <el-input v-model="activeData.class.props.label" placeholder="请输入标签键名"/>
              </el-form-item>
              <el-form-item label="值键名">
                <el-input v-model="activeData.class.props.value" placeholder="请输入值键名"/>
              </el-form-item>
              <el-form-item label="子级键名">
                <el-input v-model="activeData.class.props.children" placeholder="请输入子级键名"/>
              </el-form-item>
            </template>

            <!-- 级联选择静态树 -->
            <el-tree v-if="this.vGet(activeData,'config.dataType') === 'static'"
                     draggable
                     :data="activeData.attrs.options"
                     node-key="id"
                     :expand-on-click-node="false"
                     :render-content="renderContent"
            />
            <div v-if="activeData.config.dataType === 'static'" style="margin-left: 20px">
              <el-button
                style="padding-bottom: 0"
                icon="el-icon-circle-plus-outline"
                type="text"
                @click="addTreeItem"
              >
                添加父级
              </el-button>
            </div>
            <el-divider/>
          </template>

          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'config.optionType') !== undefined" label="选项样式">
            <el-radio-group v-model="activeData.config.optionType">
              <el-radio-button label="default">
                默认
              </el-radio-button>
              <el-radio-button label="button">
                按钮
              </el-radio-button>
            </el-radio-group>
          </el-form-item>

          <!--开关-->
          <el-form-item v-if="this.vGet(activeData,'attrs.active-color') !== undefined" label="开启颜色">
            <el-color-picker v-model="activeData.attrs['active-color']"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.inactive-color') !== undefined" label="关闭颜色">
            <el-color-picker v-model="activeData.attrs['inactive-color']"/>
          </el-form-item>

          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'config.showLabel') !== undefined
            && this.vGet(activeData,'config.labelWidth') !== undefined" label="显示标签"
          >
            <el-switch v-model="activeData.config.showLabel"/>
          </el-form-item>

          <!--评分-->
          <el-form-item v-if="this.vGet(activeData,'attrs.allow-half') !== undefined" label="允许半选">
            <el-switch v-model="activeData.attrs['allow-half']"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.show-text') !== undefined" label="辅助文字">
            <el-switch v-model="activeData.attrs['show-text']" @change="rateTextChange"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.show-score') !== undefined" label="显示分数">
            <el-switch v-model="activeData.attrs['show-score']" @change="rateScoreChange"/>
          </el-form-item>

          <!--滑块-->
          <el-form-item v-if="this.vGet(activeData,'attrs.show-stops') !== undefined" label="显示间断点">
            <el-switch v-model="activeData.attrs['show-stops']"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.range') !== undefined" label="范围选择">
            <el-switch v-model="activeData.attrs.range" @change="rangeChange"/>
          </el-form-item>

          <!--单选框与多选框-->
          <el-form-item
            v-if="this.vGet(activeData,'config.border') !== undefined && this.vGet(activeData,'config.optionType') === 'default'"
            label="是否带边框"
          >
            <el-switch v-model="activeData.config.border"/>
          </el-form-item>

          <!--颜色选择-->
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-color-picker'" label="颜色格式">
            <el-select
              v-model="activeData.attrs['color-format']"
              placeholder="请选择颜色格式"
              :style="{ width: '100%' }"
              clearable
              @change="colorFormatChange"
            >
              <el-option
                v-for="(item, index) in colorFormatOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>

          <!--公用属性-->
          <el-form-item
            v-if="this.vGet(activeData,'attrs.size') !== undefined &&
              ( this.vGet(activeData,'config.border') ||
                this.vGet(activeData,'config.tag') === 'el-color-picker' ||
                this.vGet(activeData,'config.tag') === 'el-button')"
            label="组件尺寸"
          >
            <el-radio-group v-model="activeData.attrs.size">
              <el-radio-button label="medium">
                中等
              </el-radio-button>
              <el-radio-button label="small">
                较小
              </el-radio-button>
              <el-radio-button label="mini">
                迷你
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.show-word-limit') !== undefined" label="输入统计">
            <el-switch v-model="activeData.attrs['show-word-limit']"/>
          </el-form-item>

          <!--计数器-->
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-input-number'" label="严格步数">
            <el-switch v-model="activeData.attrs['step-strictly']"/>
          </el-form-item>

          <!--级联选择-->
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-cascader'" label="任选层级">
            <el-switch v-model="activeData.class.props.checkStrictly"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-cascader'" label="是否多选">
            <el-switch v-model="activeData.class.props.multiple"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-cascader'" label="展示全路径">
            <el-switch v-model="activeData.attrs['show-all-levels']"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-cascader'" label="可否筛选">
            <el-switch v-model="activeData.attrs.filterable"/>
          </el-form-item>

          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'attrs.clearable') !== undefined" label="能否清空">
            <el-switch v-model="activeData.attrs.clearable"/>
          </el-form-item>


          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'attrs.readonly') !== undefined" label="是否只读">
            <el-switch v-model="activeData.attrs.readonly"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'attrs.disabled') !== undefined" label="是否禁用">
            <el-switch v-model="activeData.attrs.disabled"/>
          </el-form-item>

          <!--下拉选择-->
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-select'" label="能否搜索">
            <el-switch v-model="activeData.attrs.filterable"/>
          </el-form-item>
          <el-form-item v-if="this.vGet(activeData,'config.tag') === 'el-select'" label="是否多选">
            <el-switch v-model="activeData.attrs.multiple" @change="multipleChange"/>
          </el-form-item>

          <!--公用属性-->
          <el-form-item v-if="this.vGet(activeData,'config.required') !== undefined" label="是否必填">
            <el-switch v-model="activeData.config.required"/>
          </el-form-item>

          <!--行容器布局结构树模板-->
          <template v-if="this.vGet(activeData,'config.layoutTree')">
            <el-divider>布局结构树</el-divider>
            <el-tree
              :data="[activeData.config]"
              :props="layoutTreeProps"
              node-key="renderKey"
              default-expand-all
              draggable
            >
              <span slot-scope="{ node, data }">
                <span class="node-label">
                  <svg-icon class="node-icon" :icon-class="data.config?data.config.tagIcon:data.tagIcon"/>
                  {{ node.label }}
                </span>
              </span>
            </el-tree>
          </template>

          <!--公用正则表达式模板-->
          <template v-if="Array.isArray(this.vGet(activeData,'config.regList'))">
            <el-divider>正则校验</el-divider>
            <div
              v-for="(item, index) in activeData.config.regList"
              :key="index"
              class="reg-item"
            >
              <span class="close-btn" @click="activeData.config.regList.splice(index, 1)">
                <i class="el-icon-close"/>
              </span>
              <el-form-item label="表达式">
                <el-input v-model="item.pattern" placeholder="请输入正则"/>
              </el-form-item>
              <el-form-item label="错误提示" style="margin-bottom:0">
                <el-input v-model="item.message" placeholder="请输入错误提示"/>
              </el-form-item>
            </div>
            <div style="margin-left: 20px">
              <el-button icon="el-icon-circle-plus-outline" type="text" @click="addReg">
                添加规则
              </el-button>
            </div>
          </template>

        </el-form>
        <!-- 表单属性 -->
        <el-form v-show="currentTab === 'formConf'" size="small" label-width="90px">
          <el-form-item label="表单尺寸">
            <el-radio-group v-model="formConf.size">
              <el-radio-button label="medium">
                中等
              </el-radio-button>
              <el-radio-button label="small">
                较小
              </el-radio-button>
              <el-radio-button label="mini">
                迷你
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="标签对齐">
            <el-radio-group v-model="formConf.labelPosition">
              <el-radio-button label="left">
                左对齐
              </el-radio-button>
              <el-radio-button label="right">
                右对齐
              </el-radio-button>
              <el-radio-button label="top">
                顶部对齐
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="标签宽度">
            <el-input v-model.number="formConf.labelWidth" type="number" placeholder="请输入标签宽度" />
          </el-form-item>
          <el-form-item label="栅格间隔">
            <el-input-number v-model="formConf.gutter" :min="0" placeholder="栅格间隔" />
          </el-form-item>
          <el-form-item label="禁用表单">
            <el-switch v-model="formConf.disabled" />
          </el-form-item>
        </el-form>

      </el-scrollbar>
    </div>

    <!--树形选项添加对话框-->
    <treeNode-dialog :visible.sync="dialogVisible" v-bind="$attrs" title="添加选项" @commit="addNode"/>
    <!--图标对话框-->
    <icons-dialog :visible.sync="iconsVisible" :current="this.vGet(activeData,`attrs.${currentIconModel}`)" @select="setIcon"/>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import {isNumberStr} from '@/utils/formExpandDesign/index'
import IconsDialog from './IconsDialog'
import {inputComponents, selectComponents} from '../DesignConfig/config'
import TreeNodeDialog from './TreeNodeDialog'

//时间转换格式定义
const dateTimeFormat = {
  date: 'yyyy-MM-dd',
  week: 'yyyy 第 WW 周',
  month: 'yyyy-MM',
  year: 'yyyy',
  datetime: 'yyyy-MM-dd HH:mm:ss',
  daterange: 'yyyy-MM-dd',
  monthrange: 'yyyy-MM',
  datetimerange: 'yyyy-MM-dd HH:mm:ss'
}

export default {
  components: {
    draggable,
    IconsDialog,
    TreeNodeDialog
  },
  //不希望组件的根元素继承特性(没有在props声明的属性都会添加到组件根元素)
  inheritAttrs: false,
  props: ['showField', 'activeData' , 'formConf'],
  data() {
    return {
      //目前激活tab
      currentTab: 'field',
      //级联添加静态数据目前激活节点
      currentNode: null,
      //树形选项添加对话框可见
      dialogVisible: false,
      //图标对话框可见
      iconsVisible: false,
      //目前控件已选择图标
      currentIconModel: null,
      //日期选择时间类型数据
      dateTypeOptions: [
        {
          label: '日(date)',
          value: 'date'
        },
        {
          label: '周(week)',
          value: 'week'
        },
        {
          label: '月(month)',
          value: 'month'
        },
        {
          label: '年(year)',
          value: 'year'
        },
        {
          label: '日期时间(datetime)',
          value: 'datetime'
        }
      ],
      //日期范围时间类型数据
      dateRangeTypeOptions: [
        {
          label: '日期范围(daterange)',
          value: 'daterange'
        },
        {
          label: '月范围(monthrange)',
          value: 'monthrange'
        },
        {
          label: '日期时间范围(datetimerange)',
          value: 'datetimerange'
        }
      ],
      //颜色选择颜色格式数据
      colorFormatOptions: [
        {
          label: 'hex',
          value: 'hex'
        },
        {
          label: 'rgb',
          value: 'rgb'
        },
        {
          label: 'rgba',
          value: 'rgba'
        },
        {
          label: 'hsv',
          value: 'hsv'
        },
        {
          label: 'hsl',
          value: 'hsl'
        }
      ],
      //行容器水平排列数据
      justifyOptions: [
        {
          label: 'start',
          value: 'start'
        },
        {
          label: 'end',
          value: 'end'
        },
        {
          label: 'center',
          value: 'center'
        },
        {
          label: 'space-around',
          value: 'space-around'
        },
        {
          label: 'space-between',
          value: 'space-between'
        }
      ],
      //行容器布局结构树数据
      layoutTreeProps: {
        label(data, node) {
          const config = data.config
          return data.componentName || `${config.label}: ${config.vModel}`
        }
      }
    }
  },
  computed: {
    //elementUi组件操作文档
    documentLink() {
      return (this.vGet(this.activeData, 'config.document') || 'https://element.eleme.cn/#/zh-CN/component/installation')
    },
    //日期选择与日期范围组件公用数据
    dateOptions() {
      if (this.activeData.attrs.type !== undefined && this.activeData.config.tag === 'el-date-picker') {
        if (this.activeData.attrs['start-placeholder'] === undefined) {
          return this.dateTypeOptions
        }
        return this.dateRangeTypeOptions
      }
      return []
    },
    //修改组件类型数据
    tagList() {
      return [
        {
          label: '输入型组件',
          options: inputComponents
        },
        {
          label: '选择型组件',
          options: selectComponents
        }
      ]
    },
    //目前处于激活的控件标签
    activeTag() {
      return this.vGet(this.activeData, 'config.tag')
    },
    //最小值属性可见处理
    isShowMin() {
      return ['el-input-number', 'el-slider'].indexOf(this.activeTag) > -1
    },
    //最小值属性可见处理
    isShowMax() {
      return ['el-input-number', 'el-slider', 'el-rate'].indexOf(this.activeTag) > -1
    },
    //步长属性可见处理
    isShowStep() {
      return ['el-input-number', 'el-slider'].indexOf(this.activeTag) > -1
    }
  },
  methods: {
    //添加正则表达式
    addReg() {
      this.activeData.config.regList.push({
        pattern: '',
        message: ''
      })
    },
    //添加下拉选择项
    addSelectItem() {
      this.activeData.slot.options.push({
        label: '',
        value: ''
      })
    },
    //添加树形下拉项
    addTreeItem() {
      this.dialogVisible = true
      this.currentNode = this.activeData.attrs.options
    },
    //级联选择静态数据自定义渲染
    renderContent(h, {node, data, store}) {
      return (
        <div class="custom-tree-node">
          <span>{node.label}</span>
          <span class="node-operation">
            <i on-click={() => this.append(data)}
               class="el-icon-plus"
               title="添加"
            ></i>
            <i on-click={() => this.remove(node, data)}
               class="el-icon-delete"
               title="删除"
            ></i>
          </span>
        </div>
      )
    },
    //添加树形下拉子项
    append(data) {
      if (!data.children) {
        this.$set(data, 'children', [])
      }
      this.dialogVisible = true
      this.currentNode = data.children
    },
    //删除树形下拉项
    remove(node, data) {
      this.activeData.config.defaultValue = [] // 避免删除时报错
      const {parent} = node
      const children = parent.data.children || parent.data
      const index = children.findIndex(d => d.id === data.id)
      children.splice(index, 1)
    },
    //树形选项添加对话框-添加节点项
    addNode(data) {
      this.currentNode.push(data)
    },
    //设置选项值
    setOptionValue(item, val) {
      item.value = String(val)
    },
    //设置默认值
    setDefaultValue(val) {
      if (Array.isArray(val)) {
        return val.join(',')
      }
      if (typeof val === 'boolean') {
        return `${val}`
      }
      return val
    },
    //设置默认值-输入事件
    onDefaultValueInput(str) {
      if (Array.isArray(this.activeData.config.defaultValue)) {
        // 数组
        this.$set(
          this.activeData.config,
          'defaultValue',
          str.split(',').map(val => (isNumberStr(val) ? +val : val))
        )
      } else if (['true', 'false'].indexOf(str) > -1) {
        // 布尔
        this.$set(this.activeData.config, 'defaultValue', JSON.parse(str))
      } else {
        // 字符串
        this.$set(this.activeData.config, 'defaultValue',str)
      }
    },
    //设置开关对应的值
    onSwitchValueInput(val, name) {
      if (['true', 'false'].indexOf(val) > -1) {
        this.$set(this.activeData.attrs, name, JSON.parse(val))
      } else {
        this.$set(this.activeData.attrs, name, isNumberStr(val) ? +val : val)
      }
    },
    //设置时间格式
    setTimeValue(val, type) {
      const valueFormat = type === 'week' ? dateTimeFormat.date : val
      this.$set(this.activeData.config, 'defaultValue', null)
      this.$set(this.activeData.attrs, 'value-format', valueFormat)
      this.$set(this.activeData.attrs, 'format', val)
    },
    //设置多选
    multipleChange(val) {
      this.$set(this.activeData.config, 'defaultValue', val ? [] : '')
    },
    //设置时间类型
    dateTypeChange(val) {
      this.setTimeValue(dateTimeFormat[val], val)
    },
    //设置范围选择
    rangeChange(val) {
      this.$set(
        this.activeData.config,
        'defaultValue',
        val ? [this.activeData.attrs.min, this.activeData.attrs.max] : this.activeData.attrs.min
      )
    },
    //关闭分数(鱼跟熊掌不可兼得)
    rateTextChange(val) {
      if (val) this.activeData.attrs['show-score'] = false
    },
    //关闭辅助文字(鱼跟熊掌不可兼得)
    rateScoreChange(val) {
      if (val) this.activeData.attrs['show-text'] = false
    },
    //设置颜色格式
    colorFormatChange(val) {
      this.activeData.config.defaultValue = null
      this.activeData.attrs['show-alpha'] = val.indexOf('a') > -1
      this.activeData.config.renderKey = +new Date() // 更新renderKey,重新渲染该组件
    },
    //打开图标对话框
    openIconsDialog(model) {
      this.iconsVisible = true
      this.currentIconModel = model
    },
    //图标对话框-设置图标
    setIcon(val) {
      this.activeData.attrs[this.currentIconModel] = val
    },
    //修改组件类型
    tagChange(tagIcon) {
      let target = inputComponents.find(item => item.config.tagIcon === tagIcon)
      if (!target) target = selectComponents.find(item => item.config.tagIcon === tagIcon)
      this.$emit('tag-change', target)
    }
  }
}
</script>

<style lang="scss" scoped>
//右面板容器总局样式
.right-board {
  width: 350px;
  position: absolute;
  right: 0;
  top: 0;
  padding-top: 3px;
  //控件属性操作面板样式
  .field-box {
    position: relative;
    height: calc(100vh - 42px);
    box-sizing: border-box;
    overflow: hidden;
  }

  //控件属性操作面板滚动条样式
  .el-scrollbar {
    height: 100%;
  }
}

//公用选项样式(选项名|选项值)
.select-item {
  display: flex;
  border: 1px dashed #fff;
  box-sizing: border-box;
  //选项框右侧移除按钮样式
  & .close-btn {
    cursor: pointer;
    color: #f56c6c;
  }

  //设置两个选项框样式
  & .el-input + .el-input {
    margin-left: 4px;
  }
}

//设置两个选项上下间距
.select-item + .select-item {
  margin-top: 4px;
}

//设置选项移动时边框样式
.select-item.sortable-chosen {
  border: 1px dashed #409eff;
}

//选项框左边图标样式
.select-line-icon {
  line-height: 32px;
  font-size: 22px;
  padding: 0 4px;
  color: #777;
}

//设置光标图标类型
.option-drag {
  cursor: move;
}

//组件文档样式
.document-link {
  position: absolute;
  display: block;
  width: 26px;
  height: 26px;
  top: 0;
  left: 0;
  cursor: pointer;
  background: #409eff;
  z-index: 1;
  border-radius: 0 0 6px 0;
  text-align: center;
  line-height: 26px;
  color: #fff;
  font-size: 18px;
}

//布局结构树节点样式
.node-label {
  font-size: 14px;
}

//节点图标样式
.node-icon {
  color: #bebfc3;
}
</style>
