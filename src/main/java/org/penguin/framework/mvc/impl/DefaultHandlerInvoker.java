package org.penguin.framework.mvc.impl;

import org.penguin.framework.ioc.BeanHelper;
import org.penguin.framework.util.CastUtil;
import org.penguin.framework.util.ClassUtil;
import org.penguin.framework.InstanceFactory;
import org.penguin.framework.mvc.Handler;
import org.penguin.framework.mvc.HandlerInvoker;
import org.penguin.framework.mvc.UploadHelper;
import org.penguin.framework.mvc.ViewResolver;
import org.penguin.framework.mvc.bean.Params;
import org.penguin.framework.util.MapUtil;
import org.penguin.framework.util.WebUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author ： Martin
 * Date : 18/2/13
 * Description :
 * Version : 2.0
 */
public class DefaultHandlerInvoker implements HandlerInvoker {

    private ViewResolver viewResolver = InstanceFactory.getViewResolver();

    @Override
    public void invokeHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception {
        Class<?> actionClass = handler.getActionClass();
        Method actionMethod = handler.getActionMethod();
        Object actionInstance = BeanHelper.getBean(actionClass);
        List<Object> actionMethodParamList = createActionMethodParamList(request, handler);
        checkParamList(actionMethod, actionMethodParamList);
        Object actionMethodResult = invokeActionMethod(actionMethod, actionInstance, actionMethodParamList);
        viewResolver.resolveView(request, response, actionMethodResult);
    }

    public List<Object> createActionMethodParamList(HttpServletRequest request, Handler handler) throws Exception {
        List<Object> paramList = new ArrayList<Object>();
        Class<?>[] actionParamTypes = handler.getActionMethod().getParameterTypes();
        paramList.addAll(createPathParamList(handler.getRequestPathMatcher(), actionParamTypes));
        if (UploadHelper.isMultipart(request)) {
            paramList.addAll(UploadHelper.createMultipartParamList(request));
        } else {
            Map<String, Object> requestParamMap = WebUtil.getRequestParamMap(request);
            if (MapUtil.isNotEmpty(requestParamMap)) {
                paramList.add(new Params(requestParamMap));
            }
        }
        return paramList;
    }

    private List<Object> createPathParamList(Matcher requestPathMatcher, Class<?>[] actionParamTypes) {
        List<Object> paramList = new ArrayList<Object>();
        for (int i = 1; i <= requestPathMatcher.groupCount(); i++) {
            String param = requestPathMatcher.group(i);
            Class<?> paramType = actionParamTypes[i - 1];
            if (ClassUtil.isInt(paramType)) {
                paramList.add(CastUtil.castInt(param));
            } else if (ClassUtil.isLong(paramType)) {
                paramList.add(CastUtil.castLong(param));
            } else if (ClassUtil.isDouble(paramType)) {
                paramList.add(CastUtil.castDouble(param));
            } else if (ClassUtil.isString(paramType)) {
                paramList.add(param);
            }
        }
        // 返回参数列表
        return paramList;
    }

    private Object invokeActionMethod(Method actionMethod, Object actionInstance, List<Object> actionMethodParamList) throws IllegalAccessException, InvocationTargetException {
        // 通过反射调用 Action 方法
        actionMethod.setAccessible(true); // 取消类型安全检测（可提高反射性能）
        return actionMethod.invoke(actionInstance, actionMethodParamList.toArray());
    }

    private void checkParamList(Method actionMethod, List<Object> actionMethodParamList) {
        Class<?>[] actionMethodParameterTypes = actionMethod.getParameterTypes();
        if (actionMethodParameterTypes.length != actionMethodParamList.size()) {
            String message = String.format("因为参数个数不匹配，所以无法调用 Action 方法！原始参数个数：%d，实际参数个数：%d", actionMethodParameterTypes.length, actionMethodParamList.size());
            throw new RuntimeException(message);
        }
    }
}
