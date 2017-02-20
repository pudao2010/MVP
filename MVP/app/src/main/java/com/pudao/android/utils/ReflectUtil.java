package com.pudao.android.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by pucheng on 2017/2/20.
 * 反射的工具类, 根据类的泛型获得泛型的类
 */

public class ReflectUtil {

    public static <T> Class<T> getClassGeneric(Object obj, int index) {
        try {
            Type genericSuperclass = obj.getClass().getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length > index) {
                    return (Class<T>) actualTypeArguments[index];
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
