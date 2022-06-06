package com.zhangjun.classdesign.slims.service;

import com.zhangjun.classdesign.slims.exception.RoleException;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 张钧
 */
public interface ExportService {
    /**
     * 导出所有请假单
     * @param map
     * @param request
     * @param response
     */
    void getAll(ModelMap map, HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 根据条件导出请假单
     * @param map
     * @param request
     * @param response
     * @param aimPage
     * @param pageSize
     * @param clazzId
     * @param startDate
     * @param endDate
     * @throws RoleException
     */
    void getByCondition(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer aimPage, Integer pageSize, Integer clazzId, String startDate, String endDate) throws RoleException;
}
