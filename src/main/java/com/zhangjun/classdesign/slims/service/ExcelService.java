package com.zhangjun.classdesign.slims.service;

import com.zhangjun.classdesign.slims.exception.RoleException;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-06 14:00
 */
public interface ExcelService {

    /**
     *
     * @param response
     * @param aimPage
     * @param pageSize
     * @param clazzId
     * @param startDate
     * @param endDate
     * @throws RoleException
     */
    void export(HttpServletResponse response, Integer aimPage, Integer pageSize, Integer clazzId, String startDate, String endDate) throws RoleException;

}
