import { login, logout, getInfo } from '@/api/login'
import { getAccessToken, setAccessToken, removeAccessToken, getRefreshToken, setRefreshToken, removeRefreshToken } from '@/utils/auth'

const user = {
  state: {
    access_token: getAccessToken(),
    refresh_token: getRefreshToken(),
    name: '',
    avatar: '',
    roles: [],
    permissions: []
  },

  mutations: {
    SET_ACCESS_TOKEN: (state, access_token) => {
      state.access_token = access_token
    },
    SET_REFRESH_TOKEN: (state, refresh_token) => {
      state.refresh_token = refresh_token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const userName = userInfo.userName.trim()
      const password = userInfo.password
      const code = userInfo.code
      const realKey = userInfo.realKey
      const time = userInfo.time
      return new Promise((resolve, reject) => {
        login(userName, password, time, code, realKey).then(res => {
          setAccessToken(res.access_token)
          setRefreshToken(res.refresh_token)
          commit('SET_ACCESS_TOKEN', res.access_token)
          commit('SET_REFRESH_TOKEN', res.refresh_token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          const user = res.data
          const avatar = user.avatar == "" ? require("@/assets/images/profile.gif") : process.env.VUE_APP_BASE_API + user.avatar;
          if (user.roleList && user.roleList.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', user.roleList)
            commit('SET_PERMISSIONS', user.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NAME', user.userName)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_ACCESS_TOKEN', '')
          commit('SET_REFRESH_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeAccessToken()
          removeRefreshToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_ACCESS_TOKEN', '')
        commit('SET_REFRESH_TOKEN', '')
        removeAccessToken()
        removeRefreshToken()
        resolve()
      })
    }
  }
}

export default user
