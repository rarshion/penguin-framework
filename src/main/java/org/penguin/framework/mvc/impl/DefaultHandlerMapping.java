package org.penguin.framework.mvc.impl;

import org.penguin.framework.mvc.Requester;
import org.penguin.framework.mvc.ActionHelper;
import org.penguin.framework.mvc.Handler;
import org.penguin.framework.mvc.HandlerMapping;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author ： Martin
 * Date : 18/2/13
 * Description :
 * Version : 2.0
 */
public class DefaultHandlerMapping implements HandlerMapping {

    @Override
    public Handler getHandler(String currentRequestMethod, String currentRequestPath) {
        Handler handler = null;
        Map<Requester, Handler> actionMap = ActionHelper.getActionMap();
        for (Map.Entry<Requester, Handler> actionEntry : actionMap.entrySet()) {
            Requester requester = actionEntry.getKey();
            String requestMethod = requester.getRequestMethod();
            String requestPath = requester.getRequestPath(); // 正则表达式
            Matcher requestPathMatcher = Pattern.compile(requestPath).matcher(currentRequestPath);
            if (requestMethod.equalsIgnoreCase(currentRequestMethod) && requestPathMatcher.matches()) {
                handler = actionEntry.getValue();
                if (handler != null) {
                    handler.setRequestPathMatcher(requestPathMatcher);
                }
                break;
            }
        }
        return handler;
    }
}
