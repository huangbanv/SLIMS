<template>
  <el-row style="width: 100%;height: 40px">
    <el-select v-model="clazzId" style="position: absolute;left: 0" placeholder="请选择班级">
      <el-option :label="data.name" :value="data.id" :key="index" v-for="(data,index) in tinyClazzData"/>
    </el-select>
    <el-date-picker
      v-model="dateRange"
      type="daterange"
      value-format="YYYY-MM-DD"
      range-separator="至"
      style="max-width: 200px; position: absolute;left: 250px;"
      start-placeholder="开始日期"
      end-placeholder="结束日期">
    </el-date-picker>
    <div style="position: absolute;left: 50%;top: 20%">总记录：{{ this.total }}条</div>
    <el-button type="primary" style="position: absolute;right: 31%;top: 20%" size='small' @click="getData">查询</el-button>
    <el-button type="primary" style="position: absolute;right: 23%;top: 20%" size='small' @click="exportAll">导出所有</el-button>
    <el-button type="primary" style="position: absolute;right: 11%;top: 20%" size='small' @click="exportByCondition">导出查询到的所有</el-button>
    <el-button type="primary" style="position: absolute;right: 0;top: 20%" size='small' @click="exportByConditionLimit">仅导出此页记录</el-button>
  </el-row>
  <el-table
    :data="tableData"
    style="width: 100%;margin-bottom: 20px;"
    row-key="id"
    border
    lazy
  >
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
      startSearch: false,
      tinyClazzData: '',
      clazzId: '',
      dialogFormVisible: false,
      dateRange: ''
    }
  },
  methods: {
    handleSizeChange (val) {
      this.pageSize = val
      if (this.startSearch) {
        this.getData()
      } else {
        this.getAllData()
      }
    },
    handleCurrentChange (val) {
      this.aimPage = val
      if (this.startSearch) {
        this.getData()
      } else {
        this.getAllData()
      }
    },
    open (message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    getClazzTiny () {
      this.axios({
        url: '/slims/clazz/listAll',
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
          this.tinyClazzData = data.data
        } else {
          this.open(data.msg, 'error')
        }
      })
    },
    getAllData () {
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
          this.open(data.msg, 'error')
        }
      })
    },
    getData () {
      if (this.dateRange !== '') {
        this.startSearch = true
        this.axios({
          url: '/slims/leave/byClazzAndTime',
          method: 'get',
          params: {
            aimPage: this.aimPage,
            pageSize: this.pageSize,
            clazzId: this.clazzId,
            startDate: this.dateRange[0],
            endDate: this.dateRange[1]
          }
        }).then(({ data }) => {
          console.log(data)
          if (data.code === 300) {
            this.open(data.msg, 'error')
            console.log('未登录')
            this.$router.push('/')
            return
          }
          this.open(data.msg, 'info')
          console.log('赋值')
          this.tableData = data.data.records
          this.pageSize = data.data.size
          this.aimPage = data.data.current
          this.total = data.data.total
        })
      } else {
        this.open('请选择日期和班级', 'error')
      }
    },
    exportAll () {
      window.open('http://localhost:8080/slims/export')
    },
    exportByConditionLimit () {
      if (this.dateRange !== '') {
        window.open('http://localhost:8080/slims/export/byCondition?aimPage=' + this.aimPage + '&pageSize=' + this.pageSize + '&clazzId=' + this.clazzId + '&startDate=' + this.dateRange[0] + '&endDate=' + this.dateRange[1]
        )
      } else {
        this.open('请选择日期和班级', 'error')
      }
    },
    exportByCondition () {
      if (this.dateRange !== '') {
        window.open('http://localhost:8080/slims/export/byCondition?aimPage=0&pageSize=0&clazzId=' + this.clazzId + '&startDate=' + this.dateRange[0] + '&endDate=' + this.dateRange[1])
      } else {
        this.open('请选择日期和班级', 'error')
      }
    }
  },
  created () {
    this.getAllData()
    this.getClazzTiny()
  }
}
</script>
<style>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
<!--已完成-->
