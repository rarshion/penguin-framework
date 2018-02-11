package org.smart4j.chapter2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ViewResolver {
    void resolveView(HttpServletRequest request, HttpServletResponse response, Object actionMethodResult);
}
