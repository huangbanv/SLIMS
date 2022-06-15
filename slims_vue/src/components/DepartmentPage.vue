<template>
  <el-row style="width: 100%;height: 40px">
    <el-button type="primary" style="position: absolute;right: 200px" @click="showForm('添加','','put')">添加</el-button>
    <el-dialog v-model="dialogFormVisible" :title="title" style="text-align: center">
      <el-form :model="department">
        <el-form-item label="编号" label-width="120px">
          <el-input v-model="department.id" autocomplete="off" :model-value="department.id" />
        </el-form-item>
        <el-form-item label="部门名称" label-width="120px">
          <el-input v-model="department.name" autocomplete="off" :model-value="department.name"/>
        </el-form-item>
        <el-form-item label="父部门" label-width="120px">
          <el-select v-model="department.pid" placeholder="请选择父部门" :model-value="department.pid">
            <el-option label="无" value="0"/>
            <el-option :label="data.name" :value="data.id" :key="index" v-for="(data,index) in tinyData"/>
          </el-select>
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
      width="180">
    </el-table-column>
    <el-table-column
      prop="name"
      label="部门名称"
      sortable
      width="180">
    </el-table-column>
    <el-table-column
      prop="createDate"
      label="创建时间">
    </el-table-column>
    <el-table-column
      prop="updateDate"
      label="更新时间">
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
          @click="handleDelete(scope.row)">删除
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
      tinyData: '',
      title: '',
      dialogFormVisible: false,
      department: {
        id: '',
        name: '',
        pid: '',
        method: ''
      }
    }
  },
  methods: {
    closeForm () {
      this.dialogFormVisible = false
      this.title = ''
      this.department = {
        id: '',
        name: '',
        pid: '',
        method: ''
      }
    },
    showForm (title, department, method) {
      this.getTiny()
      this.dialogFormVisible = true
      this.title = title
      this.department.id = department.id
      this.department.pid = department.pid
      this.department.name = department.name
      this.department.method = method
    },
    open (message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    handleDelete (department) {
      console.log('删除--' + department.children.length + '--')
      if (Object.keys(department.children).length !== 0) {
        this.open('部门还有子部门，不可删除', 'error')
        return
      }
      this.axios({
        url: '/slims/department',
        method: 'delete',
        params: {
          id: department.id
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
      console.log(this.department)
      this.axios({
        url: '/slims/department',
        method: this.department.method === 'post' ? 'post' : 'put',
        data: {
          id: this.department.id,
          name: this.department.name,
          pid: this.department.pid
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
    getData () {
      this.axios({
        url: '/slims/department',
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
    },
    getTiny () {
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
          this.tinyData = data.data
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
