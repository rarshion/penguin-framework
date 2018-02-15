package org.penguin.framework.aop;

import org.penguin.framework.*;
import org.penguin.framework.aop.annotation.Aspect;
import org.penguin.framework.aop.annotation.AspectOrder;
import org.penguin.framework.aop.proxy.Proxy;
import org.penguin.framework.aop.proxy.ProxyManager;
import org.penguin.framework.core.ClassHelper;
import org.penguin.framework.core.ClassScanner;
import org.penguin.framework.core.falut.InitializationError;
import org.penguin.framework.ioc.BeanHelper;
import org.penguin.framework.util.ClassUtil;
import org.penguin.framework.util.StringUtil;

import java.lang.annotation.Annotation;
import java.util.*;

public class AopHelper {

    private static final ClassScanner classScanner = InstanceFactory.getClassScanner();

    static {
        try {
            Map<Class<?>, List<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                Object proxyInstance = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass, proxyInstance);
            }
        } catch (Exception e) {
            throw new InitializationError("初始化 AopHelper 出错！", e);
        }
    }

    private static Map<Class<?>, List<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, List<Class<?>>> proxyMap = new LinkedHashMap<Class<?>, List<Class<?>>>();
        addAspectProxy(proxyMap);
        return proxyMap;
    }

    private static void addAspectProxy(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
        List<Class<?>> aspectProxyClassList = ClassHelper.getClassListBySuper(AspectProxy.class);
        aspectProxyClassList.addAll(classScanner.getClassListBySuper(FrameworkConstant.PLUGIN_PACKAGE, AspectProxy.class));
        sortAspectProxyClassList(aspectProxyClassList);
        for (Class<?> aspectProxyClass : aspectProxyClassList) {
            if (aspectProxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = aspectProxyClass.getAnnotation(Aspect.class);
                List<Class<?>> targetClassList = createTargetClassList(aspect);
                proxyMap.put(aspectProxyClass, targetClassList);
            }
        }
    }

    private static void sortAspectProxyClassList(List<Class<?>> proxyClassList) {
        Collections.sort(proxyClassList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> aspect1, Class<?> aspect2) {
                if (aspect1.isAnnotationPresent(Aspect.class) || aspect2.isAnnotationPresent(Aspect.class)) {
                    if (aspect1.isAnnotationPresent(AspectOrder.class)) {
                        return getOrderValue(aspect1) - getOrderValue(aspect2);
                    } else {
                        return getOrderValue(aspect2) - getOrderValue(aspect1);
                    }
                } else {
                    return aspect1.hashCode() - aspect2.hashCode();
                }
            }
            private int getOrderValue(Class<?> aspect) {
                return aspect.getAnnotation(AspectOrder.class) != null ? aspect.getAnnotation(AspectOrder.class).value() : 0;
            }
        });
    }

    private static List<Class<?>> createTargetClassList(Aspect aspect) {
        List<Class<?>> targetClassList = new ArrayList<Class<?>>();
        String pkg = aspect.pkg();
        String cls = aspect.cls();
        Class<? extends Annotation> annotation = aspect.annotation();
        if (StringUtil.isNotEmpty(pkg)) {
            if (StringUtil.isNotEmpty(cls)) {
                targetClassList.add(ClassUtil.loadClass(pkg + "." + cls, false));
            } else {
                if (annotation != null && !annotation.equals(Aspect.class)) {
                    targetClassList.addAll(classScanner.getClassListByAnnotation(pkg, annotation));
                } else {
                    targetClassList.addAll(classScanner.getClassList(pkg));
                }
            }
        } else {
            if(annotation != null && !annotation.equals(Aspect.class)) {
                targetClassList.addAll(ClassHelper.getClassListByAnnotation(annotation));
            }
        }
        return targetClassList;
    }

    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>, List<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            List<Class<?>> targetClassList = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassList) {
                Proxy baseAspect = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(baseAspect);
                } else {
                    List<Proxy> baseAspectList = new ArrayList<Proxy>();
                    baseAspectList.add(baseAspect);
                    targetMap.put(targetClass, baseAspectList);
                }
            }
        }
        return targetMap;
    }
}
