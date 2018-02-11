package org.smart4j.chapter2;


import org.smart4j.chapter2.annotation.Impl;
import org.smart4j.chapter2.annotation.Inject;
import org.smart4j.chapter2.util.ArrayUtil;
import org.smart4j.chapter2.util.CollectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class IocHelper{

            static {
                try {
                    Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
                    for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                        Class<?> beanClass = beanEntry.getKey();
                        Object beanInstance = beanEntry.getValue();
                        Field[] beanFields = beanClass.getFields();
                        if (ArrayUtil.isNotEmpty(beanFields)) {
                            for (Field beanField : beanFields) {
                                if (beanField.isAnnotationPresent(Inject.class)) {
                                    Class<?> interfaceClass = beanField.getType();
                                    Class<?> implementClass = findImplementClass(interfaceClass);
                                    if (implementClass != null) {
                                        Object implementInstance = beanMap.get(implementClass);
                                        if (implementClass != null) {
                                            beanField.setAccessible(true);
                                            beanField.set(beanInstance, implementInstance);
                                        } else {
                                            throw new RuntimeException("依赖注入失败，类名" + beanClass.getSimpleName() + ",字段名" + interfaceClass.getSimpleName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
            throw new RuntimeException("初始化iocHelper出错", ex);
        }
    }


    public static Class<?> findImplementClass(Class<?> interfaceClass) {
        Class<?> implementClass = interfaceClass;
        if (interfaceClass.isAnnotationPresent(Impl.class)) {
            implementClass = interfaceClass.getAnnotation(Impl.class).value();
        } else {
            List<Class<?>> implementClassList = ClassHelper.getClassListBySuper(interfaceClass);
            if (CollectionUtil.isNotEmpty(implementClassList)) {
                implementClass = implementClassList.get(0);
            }
        }
        return implementClass;
    }
}
