package com.zhangjun.classdesign.slims.util;


import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 张钧
 * @Description
 * @create 2022-05-27 18:54
 */
public class EntityField {
    public static <T> List<String> getFields(Map<String,String> map, Class<T> tClass) {
        List<String> result = new ArrayList<>();
        for (Field field : tClass.getDeclaredFields()) {
            field.setAccessible(true);
            if(map.get(field.getName()) != null){
                result.add(field.getName());
            }
        }
        return result;
    }
    public static String upperCharToUnderLine(String str){
        final String regex = "[A-Z]";
        StringBuilder stringBuilder = new StringBuilder(str);
        Pattern compile = Pattern.compile(regex);
        int i = 0;
        Matcher matcher = compile.matcher(str);
        while (matcher.find()){
            stringBuilder.replace(matcher.start()+i,matcher.end()+i,"_"+matcher.group().toLowerCase());
            i++;
        }
        return stringBuilder.toString();
    }

    public static boolean roleCheck(String creator, String user){
        boolean userContains = user.contains("_");
        boolean creatorContains = creator.contains("_");
        if(userContains || creatorContains){
            String[] userRole = user.split("_");
            String[] creatorRole = creator.split("_");
            if(creatorRole.length!=2){
                return true;
            }
            if(userRole.length==1){
                return false;
            }
            if(Integer.parseInt(userRole[0]) > Integer.parseInt(creatorRole[0])){
                return Integer.parseInt(userRole[1]) == Integer.parseInt(creatorRole[1]);
            }else {
                return false;
            }
        }
        return Integer.parseInt(user) > Integer.parseInt(creator);
    }

    public static boolean isAdmin() {
        return MyInterceptor.threadLocal.get().getRoleId().equals(RoleEnum.SYSTEM_ADMIN.getCode());
    }
}
