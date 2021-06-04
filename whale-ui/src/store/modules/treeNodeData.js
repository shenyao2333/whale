const state = {
  formExtendTreeNodeData: '',
  formActivitiEngineTreeNodeData: ''
}

const mutations = {
  SET_FORM_EXTEND_TREE_NODE_DATA: (state, nodeData) => {
    state.formExtendTreeNodeData = nodeData
  },
  SET_FORM_ACTIVITI_ENGINE_TREE_NODE_DATA: (state, nodeData) => {
    state.formActivitiEngineTreeNodeData = nodeData
  }
}

const actions = {
  setFormExtendTreeNodeData({commit}, data) {
    if (Object.prototype.toString.call(data) !== "[object String]") data = JSON.stringify(data)
    commit('SET_FORM_EXTEND_TREE_NODE_DATA', data)
  },
  setFormActivitiEngineTreeNodeData({commit}, data) {
    if (Object.prototype.toString.call(data) !== "[object String]") data = JSON.stringify(data)
    commit('SET_FORM_ACTIVITI_ENGINE_TREE_NODE_DATA', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
