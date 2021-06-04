<template>
  <div class="login">
    <div class="el-login-header">
      <a href="http://www.entfrm.com">进入官网</a>
    </div>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">entfrm开发平台</h3>
      <el-form-item prop="userName">
        <el-input v-model="loginForm.userName" type="text" auto-complete="off" placeholder="账号" @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>

      <el-row>
        <el-col :span="12">
          <el-form-item prop="code" >
            <el-input v-model="loginForm.code" type="text" auto-complete="off" placeholder="验证码" maxlength="4" @keyup.enter.native="handleLogin">
              <svg-icon slot="prefix" icon-class="code" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item >
           <img :src="codeUrl" class="captcha" @click="getCode">
          </el-form-item>
        </el-col>
      </el-row>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2020-2030  entfrm团队 All Rights Reserved. <b>entfrm-ui v2.1</b></span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  data() {
    return {
      codeUrl: require('../assets/images/captcha.jpg'),
      cookiePassword: "",
      loginForm: {
        userName: "entfrm",
        password: "123456",
        rememberMe: false,
        code: "",
        time: "",
        realKey: ""
      },
      loginRules: {
        userName: [
          { required: true, trigger: "blur", message: "用户名不能为空" }
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" }
        ],
        code: [
          { required: true, trigger: "blur", message: "验证码不能为空", min: 4 }
        ]
      },
      loading: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCode();
    this.getCookie();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.codeUrl = "data:image/jpg;base64," + res.data.img;
        this.loginForm.realKey=res.data.realKey;
      });
    },
    getCookie() {
      const userName = Cookies.get("userName");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        userName: userName === undefined ? this.loginForm.userName : userName,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
        code: this.loginForm.code,
        realKey: this.loginForm.realKey
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("userName", this.loginForm.userName, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("userName");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store
            .dispatch("Login", this.loginForm)
            .then(() => {
              this.$router.push({ path: this.redirect || "/" });
            })
            .catch(() => {
              this.loading = false;
              this.loginForm.code="";
              this.loginForm.realKey="";
              this.getCode();
            });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: #E7ECF2 url("../assets/images/login_bg.svg");
  background-size: cover;
}

.el-login-header {
  height: 40px;
  line-height: 40px;
  position: fixed;
  top: 10px;
  width: 50%;
  text-align: left;
  color: #64D9D6;
  font-family: Arial;
  font-weight: bold;
  font-size: 24px;
  letter-spacing: 1px;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 14px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 10px;
  width: 100%;
  text-align: center;
  color: #707070;
  font-family: Arial;
  font-size: 16px;
  letter-spacing: 1px;
}
.captcha{
  margin-left: 20px;
}
</style>
