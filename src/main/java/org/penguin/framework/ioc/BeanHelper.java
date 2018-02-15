package org.penguin.framework.ioc;

import org.penguin.framework.core.ClassHelper;
import org.penguin.framework.core.falut.InitializationError;
import org.penguin.framework.mvc.annotation.Action;
import org.penguin.framework.aop.annotation.Aspect;
import org.penguin.framework.ioc.annotation.Bean;
import org.penguin.framework.tx.annotation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author ： Martin
 * Date : 18/2/14
 * Description :
 * Version : 2.0
 */
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
                    Object newInstance = cls.newInstance();
                    beanMap.put(cls, newInstance);
                }
            }
        } catch (Exception ex) {
            throw new InitializationError("初始化 BeanHelper 出错", ex);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return beanMap;
    }

    public static <T> T getBean(Class<T> cls) {
        if (!beanMap.containsKey(cls)) {
            throw new RuntimeException("无法根据类名获取对对象" + cls);
        }
        return (T) beanMap.get(cls);
    }

    public static void setBean(Class<?> cls, Object object) {
        beanMap.put(cls, object);
    }

}
