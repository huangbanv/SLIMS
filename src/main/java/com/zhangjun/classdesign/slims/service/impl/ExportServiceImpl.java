package com.zhangjun.classdesign.slims.service.impl;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Leave;
import com.zhangjun.classdesign.slims.excel.LeaveExcel;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.service.ExportService;
import com.zhangjun.classdesign.slims.service.LeaveService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * @author 张钧
 */
@Service
public class ExportServiceImpl implements ExportService {
    
    @Resource
    LeaveService leaveService;
    
    /**
     * 根据条件导出请假单
     */
    @Override
    public void getByCondition(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer aimPage, Integer pageSize, Integer clazzId, String startDate, String endDate) throws RoleException {
        Page<Leave> page = leaveService.listLeaveByClazzAndTime(aimPage, pageSize, clazzId, startDate, endDate);
        List<LeaveExcel> list = new ArrayList<>(page.getRecords().size());
        fillFields(page.getRecords(),list);
        exportExcel(map,request,response,list);
    }
    
    /**
     * 导出所有请假单
     */
    @Override
    public void getAll(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        Page<Leave> page = leaveService.listLeave(0,0);
        List<LeaveExcel> list = new ArrayList<>(page.getRecords().size());
        fillFields(page.getRecords(),list);
        exportExcel(map,request,response,list);
    }
    
    private void exportExcel(ModelMap map, HttpServletRequest request, HttpServletResponse response,List<LeaveExcel> list){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        ExportParams params = new ExportParams("请假列表               共："+list.size()+"条记录",null, ExcelType.XSSF);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, LeaveExcel.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "leaveList"+ LocalDateTime.now().format(formatter));
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
    
    private void fillFields(List<Leave> resource,List<LeaveExcel> target){
        resource.forEach(leave -> {
            LeaveExcel leaveExcel = new LeaveExcel();
            leaveExcel.setId(leave.getId()).setStudentId(leave.getStudentId()).setStudentName(leave.getStudentName())
                    .setInstructorId(leave.getInstructorId()).setInstructorName(leave.getInstructorName()).setStatus(leave.getStatus())
                    .setStatusS(leave.getStatusS()).setType(leave.getType()).setTypeS(leave.getTypeS()).setReason(leave.getReason())
                    .setCreateTime(leave.getCreateTime()).setEndTime(leave.getEndTime()).setDays(leave.getDays()).setStartTime(leave.getStartTime());
            target.add(leaveExcel);
        });
    }
}
