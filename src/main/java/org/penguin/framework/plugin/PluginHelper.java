package org.penguin.framework.plugin;

import org.penguin.framework.FrameworkConstant;
import org.penguin.framework.InstanceFactory;
import org.penguin.framework.core.ClassScanner;
import org.penguin.framework.core.falut.InitializationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Author ： Martin
 * Date : 18/2/14
 * Description :
 * Version : 2.0
 */
public class PluginHelper {
    private static final List<Plugin> pluginList = new ArrayList<Plugin>();
    private static final ClassScanner classScanner = InstanceFactory.getClassScanner();

    static {
        try {
            List<Class<?>> pluginClassList = classScanner.getClassListBySuper(FrameworkConstant.PLUGIN_PACKAGE, Plugin.class);
            for (Class<?> pluginClass : pluginClassList) {
                Plugin plugin = (Plugin) pluginClass.newInstance();
                plugin.init();
                pluginList.add(plugin);
            }
        } catch (Exception e) {
            throw new InitializationError("初始化 PluginHelper 出错！", e);
        }
    }

    public static List<Plugin> getPluginList() {
        return pluginList;
    }
}
