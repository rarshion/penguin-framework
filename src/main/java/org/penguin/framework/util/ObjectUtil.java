package org.penguin.framework.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    public static void setField(Object object, String fieldName, Object fieldValue) {
        try {
            if (PropertyUtils.isWriteable(object, fieldName)) {
                PropertyUtils.setProperty(object, fieldName, fieldValue);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取成员变量
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Object propertyValue = null;
        try {
            if (PropertyUtils.isReadable(obj, fieldName)) {
                propertyValue = PropertyUtils.getProperty(obj, fieldName);
            }
        } catch (Exception e) {
            logger.error("获取成员变量出错！", e);
            throw new RuntimeException(e);
        }
        return propertyValue;
    }

    /**
     * 复制所有成员变量
     */
    public static void copyFields(Object source, Object target) {
        try {
            for (Field field : source.getClass().getDeclaredFields()) {
                // 若不为 static 成员变量，则进行复制操作
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true); // 可操作私有成员变量
                    field.set(target, field.get(source));
                }
            }
        } catch (Exception e) {
            logger.error("复制成员变量出错！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过反射创建实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        T instance;
        try {
            Class<?> commandClass = ClassUtil.loadClass(className);
            instance = (T) commandClass.newInstance();
        } catch (Exception e) {
            logger.error("创建实例出错！", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 获取对象的字段映射（字段名 => 字段值），忽略 static 字段
     */
    public static Map<String, Object> getFieldMap(Object obj) {
        return getFieldMap(obj, true);
    }

    public static Map<String, Object> getFieldMap(Object object, boolean isStaticIngored) {
        Map<String, Object> fieldMap = new LinkedHashMap<String, Object>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (isStaticIngored && Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String fieldName = field.getName();
            Object fieldValue = ObjectUtil.getFieldValue(object, fieldName);
            fieldMap.put(fieldName, fieldValue);
        }
        return fieldMap;
    }
}
