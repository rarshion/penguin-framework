package org.smart4j.chapter2;

public final class HelperLoader {
    public static void init() {
        // 定义需要加载的 Helper 类
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ActionHelper.class
        };
        // 按照顺序加载类
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}
