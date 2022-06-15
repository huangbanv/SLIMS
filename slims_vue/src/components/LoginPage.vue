<template>
  <div class="common-layout">
    <el-container>
      <el-header style="background: rgb(32,137,77)">
        <el-row :gutter="20">
          <el-col :span="5">
            <div class="grid-content bg-purple background"/>
          </el-col>
          <el-col :span="15">
            <div class="grid-content bg-purple"/>
          </el-col>
        </el-row>
      </el-header>
      <el-main style="">
        <el-form
          id="form"
          label-position="left"
          label-width="60px"
          :model="loginForm"
        >
          <el-row :gutter="20" style="height: 100px;">
          </el-row>
          <el-row :gutter="20" style="left: 10%">
            <el-form-item label="账户 ：">
              <el-input v-model="loginForm.account"/>
            </el-form-item>
          </el-row>
          <el-row :gutter="20" style="left: 10%">
            <el-form-item label="密码 ：">
              <el-input v-model="loginForm.password" type="password" autocomplete="off"/>
            </el-form-item>
          </el-row>
          <el-row :gutter="20" style="left: 10%">
            <el-form-item>
              <el-button type="primary" round @click="submitForm(loginForm)">登录</el-button>
            </el-form-item>
          </el-row >
          <el-row :gutter="20" style="height: 20px;">
          </el-row>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>
<script>
export default {
  data () {
    return {
      loginForm: {
        account: '',
        password: ''
      }
    }
  },
  methods: {
    open (message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    submitForm (loginForm) {
      this.axios({
        headers: { 'Content-Type': 'application/json;charset=UTF-8' },
        url: '/slims/auth',
        method: 'post',
        data: {
          account: loginForm.account.replace(/^\s*|\s*$/g, ''),
          password: loginForm.password.replace(/^\s*|\s*$/g, '')
        }
      }).then(({ data }) => {
        console.log(data)
        if (data.code === 200) {
          this.open(data.msg, 'success')
          this.$cookies.config('60*60*24', '')
          this.$cookies.set('data', data.data)
          this.$router.push('/main/home')
        } else {
          this.open(data.msg, 'error')
        }
      })
    }
  }
}
</script>
<style>
#form {
  position: absolute;
  left: 40%;
  top: 30%;
  height: 300px;
  width: 300px;
  max-width: 460px;
  background: rgb(104,184,142);
}
</style>
<!--已完成-->
