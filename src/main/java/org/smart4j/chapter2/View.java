package org.smart4j.chapter2;

import java.util.HashMap;
import java.util.Map;

public class View extends BaseBean {
    private String path;
    private Map<String, Object> data;

    public View(String path) {
        this.path = path;
        data = new HashMap<String, Object>();
    }

    public View data(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public boolean isRedirect() {
        return path.startsWith("/");
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
