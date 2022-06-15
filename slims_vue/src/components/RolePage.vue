<template>
  <el-row style="width: 100%;height: 40px">
    <el-button type="primary" style="position: absolute;right: 200px" @click="showForm('添加','','put')">添加</el-button>
    <el-dialog v-model="dialogFormVisible" :title="title" style="text-align: center">
      <el-form :model="role">
        <el-form-item label="角色编号" label-width="120px">
          <el-input v-model="role.id" autocomplete="off" :model-value="role.id"/>
        </el-form-item>
        <el-form-item label="角色名称" label-width="120px">
          <el-input v-model="role.name" autocomplete="off" :model-value="role.name"/>
        </el-form-item>
        <el-form-item label="备注" label-width="120px">
          <el-input v-model="role.remark" autocomplete="off" :model-value="role.remark"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeForm">取消</el-button>
        <el-button type="primary" @click="handlePutOrPost">确认</el-button>
      </span>
      </template>
    </el-dialog>
    <el-dialog v-model="dialogMenuFormVisible" title="添加菜单" style="text-align: center">
      <el-form :model="role">
        <el-select v-model="menuRoleGroup.menuId" placeholder="请选择菜单" :model-value="menuRoleGroup.menuId">
          <el-option :label="data.name" :value="data.id" :key="index" v-for="(data,index) in allMenu"/>
        </el-select>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeMenuForm">取消</el-button>
        <el-button type="primary" @click="handleMenuPut">确认</el-button>
      </span>
      </template>
    </el-dialog>
    <el-dialog title="可用菜单" v-model="dialogTableVisible" width="40%">
      <el-button type="primary" style="position: absolute;right: 10%;top: 10%" @click="showMenuForm()">添加</el-button>
      <el-table :data="menus">
        <el-table-column property="menuRoleId" label="编号" width="150"></el-table-column>
        <el-table-column property="name" label="菜单名称" width="200"></el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button
              size="mini"
              type="danger"
              @click="handleMenuDelete(scope.row.menuRoleId)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </el-row>
  <el-table
    :data="tableData"
    style="width: 100%;margin-bottom: 20px;"
    row-key="id"
    border
    lazy
    :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
    <el-table-column
      prop="id"
      label="角色编号"
      sortable
      width="180">
    </el-table-column>
    <el-table-column
      prop="name"
      label="角色名称"
      sortable
      width="180">
    </el-table-column>
    <el-table-column
      prop="remark"
      label="备注"
      sortable
      width="180">
    </el-table-column>
    <el-table-column
      prop="createDate"
      label="创建时间"
      width="180">
    </el-table-column>
    <el-table-column
      label="操作">
      <template v-slot="scope">
        <el-button
          size="mini"
          @click="showForm('修改',scope.row,'post')">修改
        </el-button>
        <el-button
          size="mini"
          @click="getMenus(scope.row.id)">查看可用菜单
        </el-button>
        <el-button
          size="mini"
          type="danger"
          @click="handleDelete(scope.row.id)">删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script>
export default {
  data () {
    return {
      tableData: '',
      allMenu: '',
      title: '',
      dialogFormVisible: false,
      dialogMenuFormVisible: false,
      dialogTableVisible: false,
      menus: '',
      menuRoleGroup: {
        roleId: '',
        menuId: ''
      },
      role: {
        id: '',
        name: '',
        remark: '',
        method: ''
      }
    }
  },
  methods: {
    closeForm () {
      this.dialogFormVisible = false
      this.title = ''
      this.role = {
        id: '',
        name: '',
        pid: '',
        method: ''
      }
    },
    closeMenuForm () {
      this.dialogMenuFormVisible = false
    },
    showForm (title, role, method) {
      this.dialogFormVisible = true
      this.title = title
      this.role.id = role.id
      this.role.remark = role.remark
      this.role.name = role.name
      this.role.method = method
    },
    showMenuForm () {
      this.axios({
        url: '/slims/menu/listAll',
        method: 'get'
      }).then(({ data }) => {
        console.log(data)
        if (data.code === 300) {
          this.open(data.msg, 'error')
          console.log('未登录')
          this.$router.push('/')
          return
        }
        if (data.code === 200) {
          console.log('赋值')
          this.allMenu = data.data
          this.dialogMenuFormVisible = true
        } else {
          this.error = true
          this.message = data.msg
        }
      })
    },
    open (message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    handleDelete (id) {
      console.log('删除' + id)
      this.axios({
        url: '/slims/role',
        method: 'delete',
        params: {
          id: id
        }
      }).then(({ data }) => {
        console.log(data)
        if (data.code === 300) {
          this.open(data.msg, 'error')
          console.log('未登录')
          this.$router.push('/')
        } else if (data.code === 200) {
          this.open(data.msg, 'success')
        } else {
          this.open(data.msg, 'error')
        }
        this.getData()
      })
    },
    handleMenuDelete (id) {
      this.axios({
        url: '/slims/rolemenugroup',
        method: 'delete',
        params: {
          id: id
        }
      }).then(({ data }) => {
        console.log(data)
        if (data.code === 300) {
          this.open(data.msg, 'error')
          console.log('未登录')
          this.$router.push('/')
        } else if (data.code === 200) {
          this.open(data.msg, 'success')
        } else {
          this.open(data.msg, 'error')
        }
        this.getMenus(this.menuRoleGroup.roleId)
      })
    },
    handleMenuPut () {
      console.log(this.menuRoleGroup)
      this.axios({
        url: '/slims/rolemenugroup',
        method: 'put',
        data: {
          roleId: this.menuRoleGroup.roleId,
          menuId: this.menuRoleGroup.menuId
        }
      }).then(({ data }) => {
        if (data.code === 300) {
          this.open(data.msg, 'error')
          console.log('未登录')
          this.$router.push('/')
        } else if (data.code === 200) {
          this.open(data.msg, 'success')
          this.getMenus(this.menuRoleGroup.roleId)
          this.closeMenuForm()
        } else {
          this.open(data.msg, 'error')
        }
      })
    },
    handlePutOrPost () {
      console.log(this.role)
      this.axios({
        url: '/slims/role',
        method: this.role.method === 'post' ? 'post' : 'put',
        data: {
          id: this.role.id,
          name: this.role.name,
          remark: this.role.remark
        }
      }).then(({ data }) => {
        if (data.code === 300) {
          this.open(data.msg, 'error')
          console.log('未登录')
          this.$router.push('/')
        } else if (data.code === 200) {
          this.open(data.msg, 'success')
        } else {
          this.open(data.msg, 'error')
        }
        this.getData()
      })
      this.closeForm()
    },
    getMenus (id) {
      this.axios({
        url: '/slims/rolemenugroup',
        method: 'get',
        params: {
          id: id
        }
      }).then(({ data }) => {
        console.log(data)
        if (data.code === 300) {
          this.open(data.msg, 'error')
          console.log('未登录')
          this.$router.push('/')
          return
        }
        this.dialogTableVisible = true
        this.menus = data.data
        this.menuRoleGroup.roleId = id
      })
    },
    getData () {
      this.axios({
        url: '/slims/role',
        method: 'get'
      }).then(({ data }) => {
        console.log(data)
        if (data.code === 300) {
          this.open(data.msg, 'error')
          console.log('未登录')
          this.$router.push('/')
          return
        }
        if (data.code === 200) {
          console.log('赋值')
          this.tableData = data.data
        } else {
          this.open(data.msg, 'error')
        }
      })
    }
  },
  created () {
    this.getData()
  }
}
</script>
<style>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
<!--已完成-->
