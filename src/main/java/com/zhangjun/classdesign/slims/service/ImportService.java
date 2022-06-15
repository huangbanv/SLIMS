package com.zhangjun.classdesign.slims.service;

import com.zhangjun.classdesign.slims.exception.RoleException;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-14 22:11
 */
public interface ImportService {

    /**
     *
     * @param multipartFile
     * @return
     * @throws RoleException
     */
    boolean importUsers(MultipartFile multipartFile) throws RoleException;
}
