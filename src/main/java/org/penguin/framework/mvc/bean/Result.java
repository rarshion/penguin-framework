package org.penguin.framework.mvc.bean;

import org.penguin.framework.core.bean.BaseBean;

/**
 * Author ： Martin
 * Date : 18/2/14
 * Description :
 * Version : 2.0
 */
public class Result extends BaseBean {

    private boolean success; // 成功标志
    private int error;       // 错误代码
    private Object data;     // 相关数据

    public Result(boolean success) {
        this.success = success;
    }

    public Result error(int error) {
        this.error = error;
        return this;
    }

    public Result data(Object data) {
        this.data = data;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
