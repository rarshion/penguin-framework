package org.penguin.framework;

import org.penguin.framework.aop.AopHelper;
import org.penguin.framework.dao.DatabaseHelper;
import org.penguin.framework.ioc.BeanHelper;
import org.penguin.framework.ioc.IocHelper;
import org.penguin.framework.mvc.ActionHelper;
import org.penguin.framework.orm.EntityHelper;
import org.penguin.framework.plugin.PluginHelper;
import org.penguin.framework.util.ClassUtil;

public final class HelperLoader {
    public static void init() {
        // 定义需要加载的 Helper 类
        Class<?>[] classList = {
                DatabaseHelper.class,
                EntityHelper.class,
                ActionHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                PluginHelper.class,
        };
        // 按照顺序加载类
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}
