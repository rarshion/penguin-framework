package org.penguin.framework.mvc;

/**
 * Author ： Martin
 * Date : 18/2/13
 * Description :
 * Version : 2.0
 */
public interface HandlerMapping {

    /**
     * 获取 Handler
     *
     * @param currentRequestMethod 当前请求方法
     * @param currentRequestPath   当前请求路径
     * @return Handler
     */
    Handler getHandler(String currentRequestMethod, String currentRequestPath);
}
