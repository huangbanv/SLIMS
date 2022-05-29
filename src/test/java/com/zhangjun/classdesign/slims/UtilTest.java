package com.zhangjun.classdesign.slims;

import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.util.EntityField;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张钧
 * @Description
 * @create 2022-05-27 19:51
 */
public class UtilTest {

    @Test
    public void getFieldsTest(){
        Map<String,String> map = new HashMap<>();
        map.put("id","124");
        map.put("name","123");
        map.put("gender","1");
        map.put("remark","1");
        List<String> fields = EntityField.getFields(map, User.class);
        System.out.println(fields);
    }
    @Test
    public void upperCharToUnderLineTest(){
        String s = "abcAbdDdddDDDDDsWwqqs";
        String s1 = EntityField.upperCharToUnderLine(s);
        System.out.println(s1);
    }
}
