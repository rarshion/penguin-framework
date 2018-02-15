package org.penguin.framework.orm;

import org.penguin.framework.core.ClassHelper;
import org.penguin.framework.orm.annotation.Column;
import org.penguin.framework.orm.annotation.Entity;
import org.penguin.framework.orm.annotation.Table;
import org.penguin.framework.util.ArrayUtil;
import org.penguin.framework.util.MapUtil;
import org.penguin.framework.util.StringUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author ： Martin
 * Date : 18/2/12
 * Description :
 * Version : 2.0
 */
public class EntityHelper {

    /**
     * 实体类 => 表名
     */
    private static final Map<Class<?>, String> entityClassTableNameMap = new HashMap<Class<?>, String>();

    /**
     * 实体类 => (字段名 => 列名)
     */
    private static final Map<Class<?>, Map<String, String>> entityClassFieldMapMap = new HashMap<Class<?>, Map<String, String>>();

    static {
        List<Class<?>> entityClassList = ClassHelper.getClassListByAnnotation(Entity.class);
        for (Class<?> entityClass : entityClassList) {
            initEntityNameMap(entityClass);
            initEntityFieldMapMap(entityClass);
        }
    }

    private static void initEntityNameMap(Class<?> entityClass) {
        String tableName;
        if (entityClass.isAnnotationPresent(Table.class)) {
            tableName = entityClass.getAnnotation(Table.class).value();
        } else {
            tableName = StringUtil.camelhumpToUnderline(entityClass.getSimpleName());
        }
        entityClassTableNameMap.put(entityClass, tableName);
    }

    private static void initEntityFieldMapMap(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        if (ArrayUtil.isNotEmpty(fields)) {
            Map<String, String> fieldMap = new HashMap<String, String>();
            for (Field field : fields) {
                String fieldName = field.getName();
                String columnName;
                if (field.isAnnotationPresent(Column.class)) {
                    columnName = field.getAnnotation(Column.class).value();
                } else {
                    columnName = StringUtil.camelhumpToUnderline(fieldName);
                }
                fieldMap.put(fieldName, columnName);
            }
            entityClassFieldMapMap.put(entityClass, fieldMap);
        }
    }

    public static String getTableName(Class<?> entityClass) {
        return entityClassTableNameMap.get(entityClass);
    }

    public static Map<String, String> getFieldMap(Class<?> entityClass) {
        return entityClassFieldMapMap.get(entityClass);
    }

    public static Map<String, String> getColumnMap(Class<?> entityClass) {
        return MapUtil.invert(getFieldMap(entityClass));
    }

    public static String getColumnName(Class<?> entityClass, String fieldName) {
        String columnName = getFieldMap(entityClass).get(fieldName);
        return StringUtil.isNotEmpty(columnName) ? columnName : fieldName;
    }
}
