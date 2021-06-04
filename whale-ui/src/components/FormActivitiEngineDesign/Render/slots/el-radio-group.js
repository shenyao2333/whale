/**
 * @program: entfrm
 *
 * @description: el-radio-group控件[插槽]
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-10
 **/

export default {
  options(h, conf, key) {
    const list = []
    conf.slot.options.forEach(item => {
      if (conf.config.optionType === 'button') {
        list.push(<el-radio-button label={item.value}>{item.label}</el-radio-button>)
      } else {
        list.push(<el-radio label={item.value} border={conf.config.border}>{item.label}</el-radio>)
      }
    })
    return list
  }
}
