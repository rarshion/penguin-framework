package org.smart4j.chapter2;

import org.smart4j.chapter2.annotation.Action;
import org.smart4j.chapter2.annotation.Aspect;
import org.smart4j.chapter2.annotation.Bean;
import org.smart4j.chapter2.annotation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanHelper {

    private static final Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();

    static {
        try {
            List<Class<?>> classList = ClassHelper.getClassList();
            for (Class<?> cls : classList) {
                if (cls.isAnnotationPresent(Bean.class)
                        || cls.isAnnotationPresent(Service.class)
                        || cls.isAnnotationPresent(Action.class)
                        || cls.isAnnotationPresent(Aspect.class)) {
                    Object beanInstance = cls.newInstance();
                    beanMap.put(cls, beanInstance);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("初始化beanHepler出错", ex);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return beanMap;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if (!beanMap.containsKey(cls)) {
            throw new RuntimeException("无法根据类名获取实例" + cls);
        }
        return (T) beanMap.get(cls);
    }

    public static void setBean(Class<?> cls, Object obj) {
        beanMap.put(cls, obj);
    }

}
