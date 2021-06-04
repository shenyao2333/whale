/**
 * @program: entfrm
 *
 * @description: el-input控件[插槽]
 *
 * @author: entfrm开发团队-王翔
 *
 * @create: 2021-03-10
 **/

export default {
  prepend(h, conf, key) {
    return <template slot="prepend">{conf.slot[key]}</template>
  },
  append(h, conf, key) {
    return <template slot="append">{conf.slot[key]}</template>
  }
}
