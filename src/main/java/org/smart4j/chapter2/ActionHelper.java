package org.smart4j.chapter2;

import org.smart4j.chapter2.annotation.Action;
import org.smart4j.chapter2.annotation.Request;
import org.smart4j.chapter2.util.ArrayUtil;
import org.smart4j.chapter2.util.CollectionUtil;
import org.smart4j.chapter2.util.StringUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActionHelper {

    private static final Map<Requester, Handler> actionMap = new LinkedHashMap<Requester, Handler>();

    static {
        List<Class<?>> actionClassList = ClassHelper.getClassListByAnnotation(Action.class);
        if (CollectionUtil.isNotEmpty(actionClassList)) {
            Map<Requester, Handler> commonActionMap = new HashMap<Requester, Handler>();
            Map<Requester, Handler> regexActionMap = new HashMap<Requester, Handler>();
            for (Class<?> actionClass : actionClassList) {
                Method[] actionMethods = actionClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(actionMethods)) {
                    for (Method actionMethod : actionMethods) {
                        handleActionMethod(actionClass, actionMethod, commonActionMap, regexActionMap);
                    }
                }
            }
            actionMap.putAll(commonActionMap);
            actionMap.putAll(regexActionMap);
        }
    }

    private static void handleActionMethod(Class<?> actionClass, Method actionMethod, Map<Requester, Handler> commonActionMap,  Map<Requester, Handler> regexpActionMap) {
        if (actionMethod.isAnnotationPresent(Request.Get.class)) {
            String requestPath = actionMethod.getAnnotation(Request.Get.class).value();
            putActionMap("GET", requestPath, actionClass, actionMethod, commonActionMap, regexpActionMap);
        } else if (actionMethod.isAnnotationPresent(Request.Post.class)) {
            String requestPath = actionMethod.getAnnotation(Request.Post.class).value();
            putActionMap("POST", requestPath, actionClass, actionMethod, commonActionMap, regexpActionMap);
        } else if (actionMethod.isAnnotationPresent(Request.Put.class)) {
            String requestPath = actionMethod.getAnnotation(Request.Put.class).value();
            putActionMap("PUT", requestPath, actionClass, actionMethod, commonActionMap, regexpActionMap);
        } else if (actionMethod.isAnnotationPresent(Request.Delete.class)) {
            String requestPath = actionMethod.getAnnotation(Request.Delete.class).value();
            putActionMap("DELETE", requestPath, actionClass, actionMethod, commonActionMap, regexpActionMap);
        }
    }

    public static void putActionMap(String requestMethod, String requestPath, Class<?> actionClass,  Method actionMethod, Map<Requester, Handler> commonActionMap, Map<Requester, Handler> regexpActionMap) {
        if (requestPath.matches(".+\\{\\w+\\}.*")) {
            // 将请求路径中的占位符 {\w+} 转换为正则表达式 (\\w+)
            requestPath = StringUtil.replaceAll(requestPath, "\\{\\w+\\}", "(\\\\w+)");
            // 将 Requester 与 Handler 放入 Regexp Action Map 中
            regexpActionMap.put(new Requester(requestMethod, requestPath), new Handler(actionClass, actionMethod));
        } else {
            // 将 Requester 与 Handler 放入 Common Action Map 中
            commonActionMap.put(new Requester(requestMethod, requestPath), new Handler(actionClass, actionMethod));
        }
    }

    /**
     * 获取 Action Map
     */
    public static Map<Requester, Handler> getActionMap() {
        return actionMap;
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Requester requester = new Requester(requestMethod, requestPath);
        return actionMap.get(requester);
    }

}
