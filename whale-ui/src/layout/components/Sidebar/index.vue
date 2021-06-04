<template>
  <div :class="{'has-logo':showLogo}"
       :style="{ backgroundColor: settings.themeStyle === 'dark' ? variables.menuBg : variables.menuLightBg }">
    <logo v-if="showLogo" :collapse="isCollapse"/>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="settings.themeStyle === 'light' ? variables.menuLightBg : variables.menuBg"
        :text-color="settings.themeStyle === 'light' ? 'rgba(0,0,0,.65)' : variables.menuText"
        :unique-opened="true"
        :active-text-color="settings.theme"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="(route, index) in activeRoutes"
          :key="route.path  + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
  import {mapGetters, mapState} from "vuex";
  import Logo from "./Logo";
  import SidebarItem from "./SidebarItem";
  import variables from "@/assets/styles/variables.scss";

  export default {
    components: {SidebarItem, Logo},
    computed: {
      ...mapState(["settings"]),
      ...mapGetters(["sidebar"]),
      activeRoutes: {
        get () {
          return this.$store.state.permission.activeRoutes
        },
        set (val) {
          this.$store.commit('ACTIVE_ROUTES', val)
        }
      },
      activeMenu() {
        const route = this.$route;
        const {meta, path} = route;
        // if set path, the sidebar will highlight the path you set
        if (meta.activeMenu) {
          return meta.activeMenu;
        }
        return path;
      },
      showLogo() {
        return this.$store.state.settings.sidebarLogo;
      },
      variables() {
        return variables;
      },
      isCollapse() {
        return !this.sidebar.opened;
      }
    }
  };
</script>
