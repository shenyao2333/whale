/**
 * @program: entfrm
 *
 * @description:
 * 用于生成表单校验非空,指定正则规则的触发方式。
 * 未在此处声明无触发方式的组件将不生成rule！！
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-10
 **/

export default {
  'el-input': 'blur',
  'el-input-number': 'blur',
  'el-select': 'change',
  'el-radio-group': 'change',
  'el-checkbox-group': 'change',
  'el-cascader': 'change',
  'el-time-picker': 'change',
  'el-date-picker': 'change',
  'el-rate': 'change'
}
