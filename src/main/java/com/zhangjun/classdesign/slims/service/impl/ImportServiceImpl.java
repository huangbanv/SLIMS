package com.zhangjun.classdesign.slims.service.impl;

import com.zhangjun.classdesign.slims.exception.RoleException;
import com.zhangjun.classdesign.slims.service.ImportService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-14 22:11
 */
@Service
public class ImportServiceImpl implements ImportService {
    /**
     * @param multipartFile
     * @return
     * @throws RoleException
     */
    @Override
    public boolean importUsers(MultipartFile multipartFile) throws RoleException {
        return false;
    }
}
