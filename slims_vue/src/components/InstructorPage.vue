<template>
  <el-row style="width: 100%;height: 40px">
    <el-button type="primary" style="position: absolute;right: 200px" @click="showForm('添加','','put')">添加</el-button>
    <el-dialog v-model="dialogFormVisible" :title="title" style="text-align: center">
      <el-form :model="instructor">
        <el-form-item label="姓名" label-width="120px">
          <el-input v-model="instructor.name" autocomplete="off" :model-value="instructor.name"/>
        </el-form-item>
        <el-form-item label="账户" label-width="120px">
          <el-input v-model="instructor.account" autocomplete="off" :model-value="instructor.account"/>
        </el-form-item>
        <el-form-item label="密码" label-width="120px">
          <el-input v-model="instructor.password" autocomplete="off" :model-value="instructor.password"/>
        </el-form-item>
        <el-form-item label="部门" label-width="120px">
          <el-select v-model="instructor.departmentId" placeholder="请选择部门">
            <el-option :label="data.name" :value="data.id" :key="data.id" v-for="data in tinyDepartmentData"/>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" label-width="120px">
          <el-input v-model="instructor.phone" autocomplete="off" :model-value="instructor.phone"/>
        </el-form-item>
        <el-form-item label="性别" label-width="120px">
          <el-radio-group v-model="instructor.gender" size="small">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" label-width="120px">
          <el-radio-group v-model="instructor.status" size="small">
            <el-radio :label="0">停用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeForm">取消</el-button>
        <el-button type="primary" @click="handlePutOrPost">确认</el-button>
      </span>
      </template>
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
      label="编号"
      sortable
      width="90%">
    </el-table-column>
    <el-table-column
      prop="name"
      label="姓名"
      sortable
      width="90%">
    </el-table-column>
    <el-table-column
      prop="account"
      label="账户"
      sortable
      width="130%">
    </el-table-column>
    <el-table-column
      prop="password"
      label="密码"
      sortable
      width="90%">
    </el-table-column>
    <el-table-column
      prop="departmentName"
      label="部门名"
      sortable
      width="140%">
    </el-table-column>
    <el-table-column
      prop="phone"
      label="手机号"
      sortable
      width="120%">
    </el-table-column>
    <el-table-column
      prop="genderS"
      label="性别"
      sortable
      width="90%">
    </el-table-column>
    <el-table-column
      prop="statusS"
      label="状态"
      sortable
      width="90%">
    </el-table-column>
    <el-table-column
      prop="createDate"
      label="创建时间"
      width="170%">
    </el-table-column>
    <el-table-column
      prop="updateDate"
      label="更新时间"
      width="170%">
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
          type="danger"
          @click="handleDelete(scope.row.id)">删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <div>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="aimPage"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="pageSize"
      layout="sizes, prev, pager, next"
      :total="total">
    </el-pagination>
  </div>
</template>
<script>
export default {
  data () {
    return {
      tableData: '',
      aimPage: 1,
      pageSize: 5,
      total: 20,
      tinyDepartmentData: '',
      title: '',
      dialogFormVisible: false,
      instructor: {
        id: '',
        name: '',
        account: '',
        password: '',
        departmentName: '',
        departmentId: '',
        phone: '',
        gender: 1,
        status: 1,
        createDate: '',
        updateDate: '',
        method: ''
      }
    }
  },
  methods: {
    handleSizeChange (val) {
      this.pageSize = val
      this.getData()
    },
    handleCurrentChange (val) {
      this.aimPage = val
      this.getData()
    },
    closeForm () {
      this.dialogFormVisible = false
      this.title = ''
      this.instructor = {
        id: '',
        name: '',
        account: '',
        password: '',
        phone: '',
        gender: '',
        status: ''
      }
    },
    showForm (title, instructor, method) {
      console.log(instructor)
      this.getDepartmentTiny()
      this.dialogFormVisible = true
      this.title = title
      this.instructor = {
        id: instructor.id,
        name: instructor.name,
        account: instructor.account,
        password: instructor.password,
        phone: instructor.phone,
        gender: instructor.gender,
        status: instructor.status,
        departmentId: instructor.departmentId
      }
      this.instructor.method = method
    },
    open (message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    getDepartmentTiny () {
      this.axios({
        url: '/slims/department/list',
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
          this.tinyDepartmentData = data.data
        } else {
          this.open(data.msg, 'error')
        }
      })
    },
    handleDelete (id) {
      console.log('删除' + id)
      this.axios({
        url: '/slims/instructor',
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
    handlePutOrPost () {
      console.log(this.instructor)
      this.axios({
        url: '/slims/instructor',
        method: this.instructor.method === 'post' ? 'post' : 'put',
        data: this.instructor
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
    getData () {
      this.axios({
        url: '/slims/instructor',
        method: 'get',
        params: {
          aimPage: this.aimPage,
          pageSize: this.pageSize
        }
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
          this.tableData = data.data.records
          this.pageSize = data.data.size
          this.aimPage = data.data.current
          this.total = data.data.total
        } else {
          this.error = true
          this.message = data.msg
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
