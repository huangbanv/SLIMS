package com.zhangjun.classdesign.slims.controller;


import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.service.ExcelService;
import com.zhangjun.classdesign.slims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 张钧
 */
@RestController
@RequestMapping("/slims/export")
@Slf4j
public class ExportController {

    @Resource
    ExcelService excelService;

    @GetMapping
    public String getAll(HttpServletResponse response) {
        try {
            excelService.export(response,0,0,null,null,null);
        } catch (RoleException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("/byCondition")
    public String getByCondition(@RequestParam("aimPage") Integer aimPage,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("clazzId") Integer clazzId,
                                 @RequestParam("startDate") String startDate,
                                 @RequestParam("endDate") String endDate,
                                 HttpServletResponse response) {
        try {
            excelService.export(response,aimPage,pageSize,clazzId,startDate,endDate);
        } catch (RoleException e) {
            e.printStackTrace();
        }
        return "success";
    }

}
