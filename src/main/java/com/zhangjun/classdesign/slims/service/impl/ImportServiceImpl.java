package com.zhangjun.classdesign.slims.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.excel.UserExcel;
import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.service.ImportService;
import com.zhangjun.classdesign.slims.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-14 22:11
 */
@Service
public class ImportServiceImpl implements ImportService {
    
    @Resource
    UserService userService;
    
    /**
     * @param multipartFile
     * @return
     * @throws RoleException
     */
    @Override
    public boolean importUsers(MultipartFile multipartFile) throws RoleException {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        List<UserExcel> list;
        try {
            ExcelImportResult<UserExcel> objectExcelImportResult = ExcelImportUtil
                    .importExcelMore(multipartFile.getInputStream(), UserExcel.class, params);
            list = objectExcelImportResult.getList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<User> userList = new ArrayList<>(list.size());
        list.forEach(userExcel -> {
            User user = new User();
            user.setName(userExcel.getName()).setAccount(userExcel.getAccount())
                    .setPassword(userExcel.getPassword()).setDepartmentId(userExcel.getDepartmentId())
                    .setPhone(userExcel.getPhone()).setGender(userExcel.getGender())
                    .setStatus(userExcel.getStatus()).setRoleId(RoleEnum.STUDENT.getCode());
            userList.add(user);
        });
        return userService.putList(userList);
    }
}
