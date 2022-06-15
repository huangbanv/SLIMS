<template>
  <el-row style="width: 100%;height: 40px">
    <el-button type="primary" style="position: absolute;right: 200px" @click="showForm('添加','','put')">添加</el-button>
    <el-dialog v-model="dialogFormVisible" :title="title" style="text-align: center">
      <el-form :model="clazz">
        <el-form-item label="编号" label-width="120px">
          <el-input v-model="clazz.id" autocomplete="off" :model-value="clazz.id" disabled/>
        </el-form-item>
        <el-form-item label="班级名称" label-width="120px">
          <el-input v-model="clazz.name" autocomplete="off" :model-value="clazz.name"/>
        </el-form-item>
        <el-form-item label="辅导员" label-width="120px">
          <el-select v-model="clazz.instructorId" placeholder="请选择辅导员">
            <el-option :label="data.name" :value="data.id" :key="index" v-for="(data,index) in tinyData"/>
          </el-select>
        </el-form-item>
        <el-form-item label="年级" label-width="120px">
          <el-input v-model="clazz.grade" autocomplete="off" :model-value="clazz.grade"/>
        </el-form-item>
        <el-form-item label="开始时间" label-width="120px">
          <el-input v-model="clazz.startTime" autocomplete="off" :model-value="clazz.startTime"/>
        </el-form-item>
        <el-form-item label="结束时间" label-width="120px">
          <el-input v-model="clazz.endTime" autocomplete="off" :model-value="clazz.endTime"/>
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
      label="班级名称"
      sortable
      width="180">
    </el-table-column>
    <el-table-column
      prop="instructorName"
      label="辅导员"
      sortable
      width="180">
    </el-table-column>
    <el-table-column
      prop="startTime"
      label="开始时间">
    </el-table-column>
    <el-table-column
      prop="endTime"
      label="结束时间">
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
      title: '',
      dialogFormVisible: false,
      tinyData: '',
      clazz: {
        id: '',
        instructorId: '',
        name: '',
        grade: '',
        startTime: '',
        endTime: '',
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
      this.clazz = {
        id: '',
        instructorId: '',
        name: '',
        grade: '',
        startTime: '',
        endTime: '',
        method: ''
      }
    },
    showForm (title, clazz, method) {
      this.getTiny()
      this.dialogFormVisible = true
      this.title = title
      this.clazz = {
        id: clazz.id,
        instructorId: clazz.instructorId,
        name: clazz.name,
        grade: clazz.grade,
        startTime: clazz.startTime,
        endTime: clazz.endTime,
        method: method
      }
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
        url: '/slims/clazz',
        method: 'delete',
        params: {
          id: id
        }
      }).then(({ data }) => {
        console.log(data)
        if (data.code === 300) {
          console.log('未登录')
          this.open(data.msg, 'error')
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
      console.log(this.clazz)
      this.axios({
        url: '/slims/clazz',
        method: this.clazz.method === 'post' ? 'post' : 'put',
        data: this.clazz
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
    getTiny () {
      this.axios({
        url: '/slims/instructor/list',
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
    },
    getData () {
      this.axios({
        url: '/slims/clazz',
        method: 'get',
        params: {
          aimPage: this.aimPage,
          pageSize: this.pageSize
        }
      }).then(({ data }) => {
        console.log(this.aimPage)
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
