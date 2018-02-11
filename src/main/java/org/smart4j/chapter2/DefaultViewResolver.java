package org.smart4j.chapter2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.smart4j.framework.FrameworkConstant;
import org.smart4j.framework.mvc.UploadHelper;
import org.smart4j.framework.mvc.ViewResolver;
import org.smart4j.framework.mvc.bean.Result;
import org.smart4j.framework.mvc.bean.View;
import org.smart4j.framework.util.MapUtil;
import org.smart4j.framework.util.WebUtil;

public class DefaultViewResolver  implements ViewResolver {

    @Override
    public void resolveView(HttpServletRequest request, HttpServletResponse response, Object actionMethodResult) {
        if (actionMethodResult != null) {

            }
        }
    }
}
