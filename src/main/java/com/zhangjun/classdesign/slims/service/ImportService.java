package com.zhangjun.classdesign.slims.service;

import com.zhangjun.classdesign.slims.exception.RoleException;
import org.springframework.web.multipart.MultipartFile;

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
