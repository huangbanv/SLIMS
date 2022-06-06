package com.zhangjun.classdesign.slims.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjun.classdesign.slims.entity.Leave;
import com.zhangjun.classdesign.slims.excel.LeaveExcel;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.service.ExcelService;
import com.zhangjun.classdesign.slims.service.LeaveService;
import com.zhangjun.classdesign.slims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-06 14:01
 */
@Service
public class ExcelServiceImpl implements ExcelService {

    @Resource
    private LeaveService leaveService;

    @Override
    public void export(HttpServletResponse response,Integer aimPage, Integer pageSize, Integer clazzId, String startDate,String endDate) throws RoleException {
        Page<Leave> all ;
        if(startDate != null){
            all = leaveService.listLeaveByClazzAndTime(aimPage,pageSize,clazzId,startDate,endDate);
        }else {
            all = (leaveService.listLeave(aimPage,pageSize)) ;
        }
        // 定义excel的页签
        List<LeaveExcel> excels = new ArrayList<>();

        // 字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 构建写入对象
        ExcelWriter excelWriter = EasyExcel.write(outputStream).build();

        // 这里可以多个WriteSheet对象
        WriteSheet leave = EasyExcel.writerSheet(0, "leave").head(LeaveExcel.class).build();

        // 这里可以写多个write
        excelWriter.write(all.getRecords(),leave);

        // 写入完成
        excelWriter.finish();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String format = simpleDateFormat.format(new Date());
        String fileName = "leave" + format + ".xlsx";

        try {
            outputStream.writeTo(response.getOutputStream());
            response.setContentType("application/octet-stream");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"; filename*=utf-8''%s", fileName, fileName));
            response.setContentLengthLong(outputStream.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
