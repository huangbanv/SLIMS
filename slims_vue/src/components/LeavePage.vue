<template>
  <el-row style="width: 100%;height: 40px">
    <el-button type="primary" style="position: absolute;right: 200px" @click="showForm('添加','','put')">添加</el-button>
    <el-dialog v-model="dialogFormVisible" :title="title" style="text-align: center">
      <el-form :model="leave">
        <el-form-item label="编号" label-width="120px">
          <el-input v-model="leave.id" autocomplete="off" :model-value="leave.id" disabled/>
        </el-form-item>
        <el-form-item label="学生姓名" label-width="120px">
          <el-input v-model="leave.studentName" autocomplete="off" :model-value="leave.studentName" disabled/>
        </el-form-item>
        <el-form-item label="辅导员姓名" label-width="120px">
          <el-input v-model="leave.instructorName" autocomplete="off" :model-value="leave.instructorName" disabled/>
        </el-form-item>
        <el-form-item label="请假原因" label-width="120px">
          <el-input v-model="leave.reason" autocomplete="off" :model-value="leave.reason"/>
        </el-form-item>
        <el-form-item label="请假类型" label-width="120px">
          <el-radio-group v-model="leave.type" size="small">
            <el-radio :label="0">事假</el-radio>
            <el-radio :label="1">病假</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" label-width="120px">
          <el-radio-group v-model="leave.status" size="small" disabled>
            <el-radio :label="0">未批准</el-radio>
            <el-radio :label="1">已批准</el-radio>
            <el-radio :label="2">已拒绝</el-radio>
            <el-radio :label="3">已取消</el-radio>
            <el-radio :label="4">已销假</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择开始时间" label-width="120px">
          <el-date-picker
            v-model="startDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择日期">
          </el-date-picker>
          <el-time-picker
            arrow-control
            v-model="startTime"
            value-format="HH:mm:ss"
            placeholder="任意时间点">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="选择结束时间" label-width="120px">
          <el-date-picker
            v-model="endDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择日期">
          </el-date-picker>
          <el-time-picker
            arrow-control
            v-model="endTime"
            value-format="HH:mm:ss"
            placeholder="任意时间点">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="请假天数" label-width="120px">
          <el-input v-model="leave.days" autocomplete="off" :model-value="leave.days" disabled/>
        </el-form-item>
        <el-form-item label="创建时间" label-width="120px">
          <el-input v-model="leave.createTime" autocomplete="off" :model-value="leave.createTime" disabled/>
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
      width="80%">
    </el-table-column>
    <el-table-column
      prop="studentName"
      label="学生姓名"
      sortable
      width="120%">
    </el-table-column>
    <el-table-column
      prop="instructorName"
      label="辅导员姓名"
      sortable
      width="120%">
    </el-table-column>
    <el-table-column
      prop="typeS"
      label="请假类型"
      sortable
      width="120%">
    </el-table-column>
    <el-table-column
      prop="reason"
      label="请假原因"
      sortable
      width="120%">
    </el-table-column>
    <el-table-column
      prop="statusS"
      label="状态"
      sortable
      width="80%">
    </el-table-column>
    <el-table-column
      prop="startTime"
      label="开始时间"
      sortable
      width="180%">
    </el-table-column>
    <el-table-column
      prop="endTime"
      label="结束时间"
      sortable
      width="180%">
    </el-table-column>
    <el-table-column
      prop="days"
      label="请假时长"
      sortable
      width="120%">
    </el-table-column>
    <el-table-column
      prop="createTime"
      label="申请时间"
      sortable
      width="180%">
    </el-table-column>
    <el-table-column
      label="操作">
      <template v-slot="scope">
        <el-button
          size="mini"
          type="info"
          v-if="(scope.row.status=== 0 || scope.row.status=== 2 || scope.row.status=== 3) && roleId === '3_1' "
          @click="showForm('修改',scope.row,'post')">修改
        </el-button>
        <el-button
          size="mini"
          type="success"
          v-if="scope.row.status === 0 && roleId === '2_1'"
          @click="changeStatus(scope.row.id,1)">批准
        </el-button>
        <el-button
          size="mini"
          type="warning"
          v-if="scope.row.status === 0 && roleId === '2_1'"
          @click="changeStatus(scope.row.id,2)">拒绝
        </el-button>
        <el-button
          size="mini"
          type="warning"
          v-if="scope.row.status === 0 && roleId === '3_1' "
          @click="handleDelete(scope.row.id,1)">取消
        </el-button>
        <el-button
          size="mini"
          type="primary"
          v-if="scope.row.status === 1 && roleId === '3_1' "
          @click="changeStatus(scope.row.id,4)">销假
        </el-button>
        <el-button
          size="mini"
          type="danger"
          v-if="( scope.row.status === 0 || scope.row.status === 4 ) && roleId === '3_1' "
          @click="handleDelete(scope.row.id,0)">删除
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
      total: 0,
      tinyDepartmentData: '',
      tinyRoleData: '',
      tinyClazzData: '',
      title: '',
      dialogFormVisible: false,
      startDate: '',
      startTime: '',
      endDate: '',
      endTime: '',
      roleId: '',
      leave: {
        id: '',
        studentName: '',
        instructorName: '',
        type: '',
        reason: '',
        status: 0,
        endTime: '',
        startTime: '',
        days: '',
        createTime: '',
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
    changeStatus (id, status) {
      this.axios({
        url: '/slims/leave/status',
        method: 'post',
        params: {
          id: id,
          status: status
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
    closeForm () {
      this.dialogFormVisible = false
      this.title = ''
      this.leave = {
        id: '',
        studentName: '',
        instructorName: '',
        type: '',
        reason: '',
        status: 0,
        endTime: '',
        startTime: '',
        days: '',
        createTime: '',
        method: ''
      }
    },
    showForm (title, leave, method) {
      if (leave !== '') {
        const start = leave.startTime.split(' ')
        const end = leave.endTime.split(' ')
        this.startDate = start[0]
        this.startTime = start[1]
        this.endDate = end[0]
        this.endTime = end[1]
      }
      console.log(leave)
      this.dialogFormVisible = true
      this.title = title
      this.leave = {
        id: leave.id,
        studentName: leave.studentName,
        instructorName: leave.instructorName,
        type: leave.type,
        reason: leave.reason,
        status: leave.status,
        endTime: leave.endTime,
        startTime: leave.startTime,
        days: leave.days,
        createTime: leave.createTime
      }
      this.leave.method = method
    },
    open (message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    handleDelete (id, logicalDelete) {
      console.log('删除' + id)
      this.axios({
        url: '/slims/leave',
        method: 'delete',
        params: {
          id: id,
          logicalDelete: logicalDelete
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
      this.leave.startTime = this.startDate + ' ' + this.startTime
      this.leave.endTime = this.endDate + ' ' + this.endTime
      console.log(this.leave)
      this.axios({
        url: '/slims/leave',
        method: this.leave.method === 'post' ? 'post' : 'put',
        data: this.leave
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
        url: '/slims/leave',
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
    },
    getRole () {
      this.axios({
        url: '/slims/role/getRole',
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
          this.roleId = data.data
        } else {
          this.error = true
          this.message = data.msg
        }
      })
    }
  },
  created () {
    this.getData()
    this.getRole()
  }
}
</script>
<style>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
<!--已完成-->
