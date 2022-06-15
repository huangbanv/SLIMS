<template>
  <div class="common-layout">
    <el-container>
      <el-header style="text-align: right;font-size: 35px;background: rgb(32,137,77)">
        <el-dropdown style="color: black;font-size: large;top: 30%;">
          <span class="el-dropdown-link" >
              您好，{{this.userName}}
          </span>
          <template v-slot:dropdown>
            <el-dropdown-menu style="background: rgb(32,137,77)">
              <el-dropdown-item divided style="color: black" @click="logout()">安全退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-container>
        <el-aside width="100px">
          <el-scrollbar>
            <el-menu
              default-active="2"
              class="el-menu-vertical-demo"
              background-color="rgb(104,184,142)"
              text-color="#fff"
              active-text-color="#ffd04b">
              <el-submenu index="1">
                <el-menu-item-group index="1">
                  <el-menu-item :index="menu.id" v-for="menu in menus" :key="menu.id"
                                @click="toPage(menu.menuCode)">{{ menu.name }}
                  </el-menu-item>
                </el-menu-item-group>
              </el-submenu>
            </el-menu>
          </el-scrollbar>
        </el-aside>
        <el-main>
          <el-scrollbar>
            <router-view></router-view>
          </el-scrollbar>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
export default {
  data () {
    return {
      menus: '',
      userName: ''
    }
  },
  methods: {
    open (message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    toPage (menuCode) {
      this.$router.push('/main/' + menuCode)
    },
    logout () {
      this.axios({
        headers: { 'Content-Type': 'application/json;charset=UTF-8' },
        url: '/slims/auth',
        method: 'get'
      }).then(({ data }) => {
        this.open(data.msg, 'info')
        this.$router.push('/')
      })
    },
    check () {
      console.log(this.$cookies.get('data'))
      if (this.$cookies.get('data') === null) {
        this.open('请先登录', 'error')
        this.$router.push('/')
      } else {
        this.userName = this.$cookies.get('data').userName
        this.menus = this.$cookies.get('data').menus
      }
    }
  },
  mounted () {
    this.check()
  }
}
</script>
<!--已完成-->
