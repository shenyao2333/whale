import { constantRoutes } from '@/router'
import { getMenus } from '@/api/menu'
import Layout from '@/layout/index'

const permission = {
  state: {
    apps: [],
    routes: [],
    addRoutes: [],
    activeRoutes: []
  },
  mutations: {
    SET_APPS: (state, apps) => {
      state.apps = apps
    },
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    ACTIVE_ROUTES: (state, activeRoutes) => {
      state.activeRoutes = activeRoutes
    }
  },
  actions: {
    // 生成路由
    GenerateRoutes({ commit }) {
      return new Promise(resolve => {
        // 向后端请求路由数据
        getMenus().then(res => {
          const apps = res.data.applications
          commit('SET_APPS', apps)
          const accessedRoutes = filterAsyncRouter(res.data.menus)
          accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
          commit('SET_ROUTES', accessedRoutes)
          resolve(accessedRoutes)
        })
      })
    }
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap) {
  return asyncRouterMap.filter(route => {
    if (route.component) {
      // Layout组件特殊处理
      if (route.component === '#') {
        if (route.path.indexOf("/") === 0) {
          route.component = Layout
        } else {
          route.component = {render: h => h("router-view")}
        }
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    }
    return true
  })
}

export const loadView = (view) => { // 路由懒加载
  return (resolve) =>  require([`@/views/${view}`], resolve)
}

export default permission
