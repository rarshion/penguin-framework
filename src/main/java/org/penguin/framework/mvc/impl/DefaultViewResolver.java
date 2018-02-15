package org.penguin.framework.mvc.impl;

import org.penguin.framework.FrameworkConstant;
import org.penguin.framework.mvc.UploadHelper;
import org.penguin.framework.mvc.ViewResolver;
import org.penguin.framework.mvc.bean.Result;
import org.penguin.framework.mvc.bean.View;
import org.penguin.framework.util.MapUtil;
import org.penguin.framework.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class DefaultViewResolver  implements ViewResolver {

    @Override
    public void resolveView(HttpServletRequest request, HttpServletResponse response, Object actionMethodResult) {
        if (actionMethodResult != null) {
            if (actionMethodResult instanceof View) {
                View view = (View) actionMethodResult;
                if (view.isRedirect()) {
                    String path = view.getPath();
                    WebUtil.redirectRequest(path, request, response);
                } else {
                    String path = FrameworkConstant.JSP_PATH + view.getPath();
                    Map<String, Object> data = view.getData();
                    if (MapUtil.isNotEmpty(data)) {
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            request.setAttribute(entry.getKey(), entry.getValue());
                        }
                    }
                    WebUtil.forwardRequest(path, request, response);
                }
            } else {
                Result result = (Result) actionMethodResult;
                if (UploadHelper.isMultipart(request)) {
                    WebUtil.writeHTML(response, result);
                } else {
                    WebUtil.writeJSON(response, result);
                }
            }
        }
    }
}
