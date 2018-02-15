package org.penguin.framework.ioc;

import org.penguin.framework.core.ClassHelper;
import org.penguin.framework.core.falut.InitializationError;
import org.penguin.framework.ioc.annotation.Impl;
import org.penguin.framework.ioc.annotation.Inject;
import org.penguin.framework.util.ArrayUtil;
import org.penguin.framework.util.CollectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Author ： Martin
 * Date : 18/2/14
 * Description :
 * Version : 2.0
 */
public class IocHelper {

    static {
        try {
            Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    for (Field beanField : beanFields) {
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            Class<?> interfaceClass = beanField.getType();
                            Class<?> implementClass = findImplementClass(interfaceClass);
                            if (implementClass != null) {
                                Object implementInstance = beanMap.get(implementClass);
                                if (implementInstance != null) {
                                    beanField.setAccessible(true);
                                    beanField.set(beanInstance, implementInstance);
                                } else {
                                    throw new InitializationError("依赖注入失败!" + beanClass.getSimpleName() + ", 字段名" +
                                            interfaceClass.getSimpleName());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new InitializationError("初始化 IocHelper 出错！", ex);
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
